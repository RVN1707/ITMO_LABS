package com.example.backend.service;

import com.example.backend.model.PointResult;
import com.example.backend.model.User;
import com.example.backend.repository.PointResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointResultRepository pointResultRepository;
    private static final double[] ALLOWED_X_VALUES = {-5, -4, -3, -2, -1, 0, 1, 2, 3};
    private static final double[] ALLOWED_R_VALUES = {1.0, 1.5, 2.0, 2.5, 3.0};

    public boolean checkPoint(double x, double y, double r) {
        long startTime = System.nanoTime();
        boolean result = false;

        if (!isValidX(x) || !isValidY(y) || !isValidR(r)) {
            return false;
        }

        if (x >= 0 && y >= 0) {
            result = (x * x + y * y) <= r * r;
        }

        else if (x <= 0 && y >= 0) {
            result = (y <= x + r);
        }

        else if (x <= 0 && y <= 0) {
            result = (x >= -r && x <= 0) && (y >= -r && y <= 0);
        }

        else {
            result = false;
        }

        long executionTime = System.nanoTime() - startTime;
        return result;
    }

    public PointResult saveResult(double x, double y, double r, boolean hit, long executionTime, User user) {
        PointResult pointResult = new PointResult(x, y, r, hit, executionTime, user);
        return pointResultRepository.save(pointResult);
    }

    public List<PointResult> getUserResults(User user) {
        return pointResultRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void clearUserResults(User user) {
        pointResultRepository.deleteByUser(user);
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public String formatExecutionTime(long nanoTime) {
        if (nanoTime < 1000) {
            return nanoTime + " нс";
        } else if (nanoTime < 1_000_000) {
            return String.format("%.2f мкс", nanoTime / 1000.0);
        } else if (nanoTime < 1_000_000_000) {
            return String.format("%.2f мс", nanoTime / 1_000_000.0);
        } else {
            return String.format("%.2f с", nanoTime / 1_000_000_000.0);
        }
    }

    private boolean isValidX(double x) {
        for (double allowedX : ALLOWED_X_VALUES) {
            if (Math.abs(x - allowedX) < 0.0001) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidY(double y) {
        return y >= -3 && y <= 3;
    }

    private boolean isValidR(double r) {
        for (double allowedR : ALLOWED_R_VALUES) {
            if (Math.abs(r - allowedR) < 0.0001) {
                return true;
            }
        }
        return false;
    }

}