package org.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Считываем путь к файлу из переменной окружения
        String filePathEnv = System.getenv("LAB");
        if (filePathEnv == null || filePathEnv.trim().isEmpty()) {
            System.err.println("Ошибка: Переменная окружения LAB не установлена.");
            return;
        }

        Path hostFilePath = Paths.get(filePathEnv.trim());

        // 2. Читаем имя хоста из файла без BufferedReader
        String hostName;
        try {
            hostName = Files.readString(hostFilePath).trim(); // Используем Files.readString()
            if (hostName.isEmpty()) {
                System.err.println("Ошибка: Файл хоста пуст.");
                return;
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла хоста по пути: " + hostFilePath);
            e.printStackTrace();
            return;
        }

        // 3. Пользователь вводит порт
        int port = 0;
        boolean validPort = false;

        // Проверка корректности ввода порта
        while (!validPort) {
            try {
                port = Integer.parseInt(scanner.nextLine().trim());
                if (port >= 1024 && port <= 65535) {
                    validPort = true;
                } else {
                    System.out.println("Порт должен быть в диапазоне от 1024 до 65535. Попробуйте снова:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите числовое значение порта:");
            }
        }

        // 4. Создаём клиент и запускаем
        try {
            InetAddress serverAddress = InetAddress.getByName(hostName);
            ClientLogic clientLogic = new ClientLogic(serverAddress, port);
            clientLogic.run();
        } catch (UnknownHostException e) {
            System.err.println("Ошибка: Не удалось разрешить имя хоста '" + hostName + "'. Хост не найден.");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}