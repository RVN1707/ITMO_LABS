package org.example.RuntimeParsers;

import org.example.ParserInterface;
import java.util.Scanner;
import java.util.function.Predicate;

public abstract class RuntimeParser<T> implements ParserInterface {
    private final Scanner scanner = new Scanner(System.in);

    public String askString(String fieldName, String restrictions, Predicate<String> validator) {
        while (true) {
            System.out.print("Введите " + fieldName + restrictions + ":\n>>> ");
            String input = scanner.nextLine().trim();
            if (validator.test(input)) {
                return input;
            } else {
                if (input.isEmpty() && validator.test("")) {
                    return null;
                }
                System.err.println("Неверный формат ввода");
            }
        }
    }

    public Integer askInteger(String fieldName, String restrictions, Predicate<Integer> validator) {
        while (true) {
            System.out.print("Введите " + fieldName + restrictions + ":\n>>> ");
            String input = scanner.nextLine().trim();
            try {
                Integer number = Integer.parseInt(input);
                if (validator.test(number)) {
                    return number;
                } else {
                    System.err.println("Ошибка валидации");
                }
            } catch (NumberFormatException e) {
                if (input.isEmpty() && validator.test(null)) {
                    return null;
                }
                System.err.println("Неверный формат ввода");
            }
        }
    }

    public Long askLong(String fieldName, String restrictions, Predicate<Long> validator) {
        while (true) {
            System.out.print("Введите " + fieldName + restrictions + ":\n>>> ");
            String input = scanner.nextLine().trim();
            try {
                Long number = Long.parseLong(input);
                if (validator.test(number)) {
                    return number;
                } else {
                    System.err.println("Ошибка валидации");
                }
            } catch (NumberFormatException e) {
                if (input.isEmpty() && validator.test(null)) {
                    return null;
                }
                System.err.println("Неверный формат ввода");
            }
        }
    }

    public Double askDouble(String fieldName, String restrictions, Predicate<Double> validator) {

        while (true) {
            System.out.print("Введите " + fieldName + restrictions + ":\n>>> ");
            String input = scanner.nextLine().trim();
            try {
                Double number = Double.parseDouble(input);
                if (validator.test(number)) {
                    return number;
                } else {
                    System.err.println("Ошибка валидации");
                }
            } catch (NumberFormatException e) {
                if (input.isEmpty() && validator.test(null)) {
                    return null;
                }
                System.err.println("Неверный формат ввода");
            }
        }

    }

    public Float askFloat(String fieldName, String restrictions, Predicate<Float> validator) {
        while (true) {
            System.out.print("Введите " + fieldName + restrictions + ":\n>>> ");
            String input = scanner.nextLine().trim();
            try {
                Float number = Float.parseFloat(input);
                if (validator.test(number)) {
                    return number;
                } else {
                    System.err.println("Ошибка валидации");
                }
            } catch (NumberFormatException e) {
                if (input.isEmpty() && validator.test(null))
                    return null;
            }
            System.err.println("Неверный формат ввода");
        }
    }
}