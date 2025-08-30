package org.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Получаем путь к файлу из переменной окружения LAB
        String filePathStr = System.getenv("LAB");
        if (filePathStr == null || filePathStr.trim().isEmpty()) {
            System.out.println("Ошибка: Переменная окружения LAB не установлена или пуста.");
            return;
        }

        Path filePath = Paths.get(filePathStr);

        // Проверяем, существует ли файл и доступен ли для чтения
        if (!Files.exists(filePath)) {
            System.out.println("Ошибка: Файл не существует: " + filePathStr);
            return;
        }
        if (!Files.isReadable(filePath)) {
            System.out.println("Ошибка: Нет прав на чтение файла: " + filePathStr);
            return;
        }

        // Читаем все строки из файла
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }

        if (lines.isEmpty() || lines.get(0).trim().isEmpty()) {
            System.out.println("Ошибка: Файл пуст или первая строка пустая.");
            return;
        }

        String serverHost = lines.get(0).trim();

        // Ввод порта с клавиатуры
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

        // Разрешаем имя хоста
        try {
            InetAddress serverAddress = InetAddress.getByName(serverHost);
            System.out.println("Подключение к серверу: " + serverAddress.getHostAddress() + ":" + port);

            ClientLogic clientLogic = new ClientLogic(serverAddress, port);
            clientLogic.run();

        } catch (UnknownHostException e) {
            System.out.println("Ошибка: Не удалось разрешить адрес '" + serverHost + "'. Проверьте имя или IP в файле.");
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода при подключении к серверу: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка: Класс не найден при обработке данных: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}