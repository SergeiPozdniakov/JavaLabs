package com.clouds.cloud_sprint.services;

import com.clouds.cloud_sprint.FileRepository;
import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.storage.base-path:${user.home}/cloud_storage}")
    private String storageBasePath;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void setStorageBasePath(String storageBasePath) {
        this.storageBasePath = storageBasePath;
    }

    public Users createUser(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        // Создаем путь с помощью Path API
        Path basePath = Paths.get(storageBasePath);
        Path userFolderPath = basePath.resolve(users.getUsername());
        users.setBaseFolderPath(userFolderPath.toString());

        try {
            Files.createDirectories(userFolderPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create user folder: " + userFolderPath, e);
        }

        return userRepository.save(users);
    }

    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Новый метод
    @Transactional
    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Сначала удаляем все файлы пользователя через FileService
        // (это обеспечит удаление как из БД, так и из файловой системы)
        List<File> userFiles = fileRepository.findByUser(user);
        for (File file : userFiles) {
            try {
                fileService.deleteFile(file.getId()).get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                logger.error("Failed to delete file during user deletion: " + file.getId(), e);
                // Можно выбрать: либо пробрасывать исключение, либо логировать и продолжать
                throw new RuntimeException("Failed to delete user files", e);
            }
        }

        // Удаляем папку пользователя из файловой системы
        try {
            Path userFolderPath = Paths.get(user.getBaseFolderPath());
            if (Files.exists(userFolderPath)) {
                Files.walk(userFolderPath)
                        .sorted((a, b) -> -a.compareTo(b)) // удаляем от внутренних файлов к корневой папке
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                logger.warn("Failed to delete path during user folder cleanup: " + path, e);
                            }
                        });
            }
        } catch (IOException e) {
            logger.error("Failed to delete user folder: " + user.getBaseFolderPath(), e);
            throw new RuntimeException("Failed to delete user folder", e);
        }

        // Finally delete the user from database
        userRepository.delete(user);
    }
}
