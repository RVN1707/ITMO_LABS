package org.example;

import org.example.Managers.DBManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class RunServer {
    public static void main(String[] args) {
        // Получаем путь к файлу из переменной окружения LAB
        String labFilePath = System.getenv("LAB");

        if (labFilePath == null || labFilePath.isEmpty()) {
            System.err.println("Ошибка: Переменная окружения LAB не установлена.");
            System.exit(1);
        }

        Path path = Paths.get(labFilePath);

        // Проверяем существование файла
        if (!Files.exists(path)) {
            System.err.println("Ошибка: Файл по пути из переменной LAB не существует: " + labFilePath);
            System.exit(1);
        }

        try (Scanner scanner = new Scanner(System.in)) {
            // Читаем строки из файла
            List<String> lines = Files.readAllLines(path);

            if (lines.size() < 3) {
                System.err.println("Ошибка: Файл должен содержать как минимум 3 строки: URL, user, password");
                System.exit(1);
            }

            String url = lines.get(0).trim();
            String user = lines.get(1).trim();
            String password = lines.get(2).trim();

            // Устанавливаем соединение с БД
            DBManager.establishConnection(url, user, password);

            // Запрашиваем порт у пользователя
            int port;
            while (true) {
                System.out.print("Введите порт для запуска сервера (1024-65535): ");
                String input = scanner.nextLine().trim();
                try {
                    port = Integer.parseInt(input);
                    if (port >= 1024 && port <= 65535) {
                        break;
                    } else {
                        System.out.println("Порт должен быть в диапазоне от 1024 до 65535.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат. Введите число.");
                }
            }

            // Запускаем сервер с введённым портом
            ServerLogic server = new ServerLogic(port);
            server.run();

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла конфигурации: " + e.getMessage());
            System.exit(1);
        }
    }
}