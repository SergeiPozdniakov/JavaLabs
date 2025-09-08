package com.clouds.cloud_sprint.services;

import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.storage.base-path:${user.home}/cloud_storage}")
    private String storageBasePath;


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
}
