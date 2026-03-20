package com.example.backend.service;

import com.example.backend.model.PointResult;
import com.example.backend.model.User;
import com.example.backend.repository.PointResultRepository;
import com.example.backend.validation.PointValidator;
import com.example.backend.validation.PointChecker;
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
    private final PointValidator pointValidator;
    private final PointChecker pointChecker;

    public boolean checkPoint(double x, double y, double r) {
        // Сначала валидация
        if (!pointValidator.isValidX(x) || !pointValidator.isValidY(y) || !pointValidator.isValidR(r)) {
            throw new IllegalArgumentException("Некорректные значения координат");
        }

        long startTime = System.nanoTime();
        boolean result = pointChecker.checkPoint(x, y, r);
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
}