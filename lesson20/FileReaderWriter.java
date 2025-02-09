package lesson20;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FileReaderWriter {

    public static void main(String[] args) {
        // Считываем исходные данные: имя файла для записи, директорию и текст для записи в файл
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя файла: ");
        String fileName = scanner.nextLine();

        System.out.println("Введите путь к директории: ");
        String directoryPath = scanner.nextLine();

        System.out.println("Введите текст для сохранения в файл " + fileName + " : ");
        String text = scanner.nextLine();

        //Создаем новый объект типа File для создания директории если её не существует
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Директория создана: " + directoryPath);
            } else {
                System.out.println("Ошибка, не удалось создать директорию.");
                return;
            }

            //Проверка - является ли путь директорией
            if (!directory.isDirectory()) {
                System.out.println("Указанный путь не является директорией.");
                return;
            }
        }

        try {
            Path savedFilePath = saveFile(fileName, directoryPath, text);
            System.out.println("Файл сохранен : " + savedFilePath);
            System.out.println("Размер файла : " + Files.size(savedFilePath) + " байт");
            System.out.println("Время записи : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            System.out.println("Хотите перезаписать файл? (y/n): ");
            String overwrite = scanner.nextLine();
            if(overwrite.equalsIgnoreCase("y")) {
                savedFilePath = saveFile(fileName, directoryPath, text);
                System.out.println("Файл перезаписан.");
            }

            System.out.println("Введите имя файла для поиска: ");
            String searchFileName = scanner.nextLine();
            System.out.println("Введите директорию для поиска");
            String searchDirectoryName = scanner.nextLine();

            String fileContent = findAndReadFile(searchFileName, searchDirectoryName);
            if (fileContent != null) {
                System.out.println("Содержимое файла: \n" + fileContent);
            } else {
                System.out.println("Файл не найден.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static Path saveFile(String filename, String directoryPath, String text) throws IOException {
        Path filePath = Paths.get(directoryPath, filename);
        try (Writer writer = new FileWriter(filePath.toFile())) {
            writer.write(text);
        }
        return filePath;
    }

    private static String findAndReadFile(String filename, String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return null;
        }
        File[] files = directory.listFiles((dir, name) -> name.equals(filename));
        if (files != null && files.length > 0) {
            StringBuilder content = new StringBuilder();
            try (Reader reader = new FileReader(files[0])) {
                char[] buffer = new char[1024];
                int count;
                while ((count = reader.read(buffer)) != -1) {
                    content.append(buffer, 0, count);
                }
            }
            return content.toString();
        }
        return null;
    }






}
