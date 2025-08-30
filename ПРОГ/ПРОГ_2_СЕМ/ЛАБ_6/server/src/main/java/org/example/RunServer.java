package org.example;

import java.util.Scanner;

public class RunServer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите порт для сервера: ");
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

        ServerLogic serverLogic = new ServerLogic(port, "LAB");
        serverLogic.run();

        scanner.close();
    }
}