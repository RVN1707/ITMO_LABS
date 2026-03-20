package com.example.backend.validation;

import org.springframework.stereotype.Component;

@Component
public class PointValidator {

    private static final double[] ALLOWED_X_VALUES = {-5, -4, -3, -2, -1, 0, 1, 2, 3};
    private static final double[] ALLOWED_R_VALUES = {1.0, 1.5, 2.0, 2.5, 3.0};

    public boolean isValidX(double x) {
        for (double allowedX : ALLOWED_X_VALUES) {
            if (Math.abs(x - allowedX) < 0.0001) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidY(double y) {
        return y >= -3 && y <= 3;
    }

    public boolean isValidR(double r) {
        for (double allowedR : ALLOWED_R_VALUES) {
            if (Math.abs(r - allowedR) < 0.0001) {
                return true;
            }
        }
        return false;
    }
}