package org.example.utils;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Set;

@ApplicationScoped
public class Validator {

    private final Set<Double> ALLOWED_X_VALUES = Set.of(
            -5.0, -4.0, -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0
    );

    public boolean isValid(Double x, Double y, Double r) {
        return isXValid(x) && isYValid(y) && isRValid(r);
    }

    public boolean isXValid(Double x) {
        return x != null && ALLOWED_X_VALUES.contains(x);
    }

    public boolean isYValid(Double y) {
        return y != null && y >= -3 && y <= 3;
    }

    public boolean isRValid(Double r) {
        if (r == null || r <= 0) return false;

        if (r < 1 || r > 3) return false;

        double rounded = Math.round(r * 2) / 2.0;
        return Math.abs(r - rounded) < 0.001;
    }
}