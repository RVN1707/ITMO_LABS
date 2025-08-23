package Gutter;
import java.util.ArrayList;
import java.util.Scanner;

import Enums.Corner;
import exceptions.NotGutter;

public class Gutter{

    private Corner corner;
    public Gutter() throws NotGutter {
        corner = HaveBumps();
    }
    public Corner getCorner(){
        return corner;
    }
    private Corner HaveBumps() throws NotGutter {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> corners = new ArrayList<>();

        System.out.println("Введите значения высоты желоба от начала до конца (для завершения введите 'стоп')");

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("стоп")) {
                break;
            }
            try {
                int number = Integer.parseInt(input);
                corners.add(number);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите корректное целое число или 'стоп' для завершения.");
            }
        }
        return checkCorners(corners);

    }
    private Corner checkCorners(ArrayList<Integer> GutterHeights) throws NotGutter {
        if (GutterHeights == null ||GutterHeights.size() <= 1) {
            throw new NotGutter("Спуск невозможен, путь по желобу только вверх или желоб плоский");
        }

        boolean hasAscent = false;
        boolean hasDescent = false;

        for (int i = 1; i < GutterHeights.size(); i++) {
            if (GutterHeights.get(i) > GutterHeights.get(i - 1)) {
                hasAscent = true;
            } else if (GutterHeights.get(i) < GutterHeights.get(i - 1)) {
                hasDescent = true;
            }
        }

        if (!hasDescent) {
            throw new NotGutter("Спуск невозможен, путь по желобу только вверх или желоб плоский");
        } else if (hasDescent && !hasAscent) {
            return Corner.SMOOTH;
        } else {
            return Corner.BUMPLY;
        }
    }
}
