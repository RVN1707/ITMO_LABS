package org.example.utils;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AreaChecker {

    public boolean checkPoint(double x, double y, double r) {
        // Проверяем, что радиус положительный
        if (r <= 0) return false;

        // 1. Круг в 1-й четверти
        if (x >= 0 && y >= 0 && (x * x + y * y) <= r * r) {
            return true;
        }

        // 2. Треугольник во 2-й четверти
        if (x <= 0 && y >= 0 && y <= x + r) {
            return true;
        }

        // 3. Прямоугольник в 3-й четверти
        if (x >= -r/2 && x <= 0 && y >= -r && y <= 0) {
            return true;
        }

        return false;
    }
}