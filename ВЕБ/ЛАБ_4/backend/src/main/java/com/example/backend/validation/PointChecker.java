package com.example.backend.validation;

import org.springframework.stereotype.Component;

@Component
public class PointChecker {

    public boolean checkPoint(double x, double y, double r) {
        if (x >= 0 && y >= 0) {
            return (x * x + y * y) <= r * r;
        } else if (x <= 0 && y >= 0) {
            return (y <= x + r);
        } else if (x <= 0 && y <= 0) {
            return (x >= -r && x <= 0) && (y >= -r && y <= 0);
        } else {
            return false;
        }
    }
}