package org.example;

public class TargetChecker {

    public static boolean checkHit(double x, double y, double r) {
        // Проверка прямоугольной области (левый верхний квадрат)
        if (x >= -r/2 && x <= 0 && y >= 0 && y <= r) {
            return true;
        }

        // Проверка треугольной области (правый нижний треугольник)
        if (x >= 0 && x <= r/2 && y <= 0 && y >= -r && y >= -2*x - r) {
            return true;
        }

        // Проверка круговой области (левый нижний квадрант)
        if (x <= 0 && y <= 0 && (x*x + y*y) <= (r/2)*(r/2)) {
            return true;
        }

        return false;
    }
}