package org.example;

import java.util.Arrays;
import java.util.List;

public class PointValidator {
    // Допустимые значения для координаты X
    private static final List<Double> VALID_X_VALUES = Arrays.asList(-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0);

    // Допустимый диапазон для координаты Y
    private static final double Y_MIN = -3.0;
    private static final double Y_MAX = 3.0;

    // Допустимые значения для радиуса R
    private static final List<Double> VALID_R_VALUES = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0);

    public static ValidationResult validate(PointRequest request) {
        if (request == null) {
            return ValidationResult.error("Запрос не может быть пустым");
        }

        if (request.getX() == null || !VALID_X_VALUES.contains(request.getX())) {
            return ValidationResult.error("Неверное значение X. Допустимые значения: " + VALID_X_VALUES);
        }

        if (request.getY() == null || request.getY() < Y_MIN || request.getY() > Y_MAX) {
            return ValidationResult.error("Неверное значение Y. Допустимый диапазон: от " + Y_MIN + " до " + Y_MAX);
        }

        if (request.getR() == null || !VALID_R_VALUES.contains(request.getR())) {
            return ValidationResult.error("Неверное значение R. Допустимые значения: " + VALID_R_VALUES);
        }

        return ValidationResult.valid();
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult error(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
}