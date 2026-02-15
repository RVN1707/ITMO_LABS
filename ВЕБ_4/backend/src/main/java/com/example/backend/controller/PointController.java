package com.example.backend.controller;

import com.example.backend.dto.PointRequestDTO;
import com.example.backend.dto.PointResponseDTO;
import com.example.backend.model.PointResult;
import com.example.backend.model.User;
import com.example.backend.service.PointService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        String username = authentication.getName();
        return userService.getUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @PostMapping("/check")
    public ResponseEntity<PointResponseDTO> checkPoint(@RequestBody PointRequestDTO request,
                                                       HttpServletRequest httpRequest) {
        try {
            User currentUser = getCurrentUser();

            boolean hit = pointService.checkPoint(request.getX(), request.getY(), request.getR());

            long startTime = System.nanoTime();
            hit = pointService.checkPoint(request.getX(), request.getY(), request.getR());
            long executionTime = System.nanoTime() - startTime;

            PointResult savedResult = pointService.saveResult(
                    request.getX(),
                    request.getY(),
                    request.getR(),
                    hit,
                    executionTime,
                    currentUser
            );

            PointResponseDTO response = new PointResponseDTO();
            response.setX(savedResult.getX());
            response.setY(savedResult.getY());
            response.setR(savedResult.getR());
            response.setHit(savedResult.getHit());
            response.setFormattedTime(pointService.formatDateTime(savedResult.getCreatedAt()));
            response.setFormattedExecutionTime(pointService.formatExecutionTime(savedResult.getExecutionTime()));
            response.setColor(savedResult.getHit() ? "green" : "red");
            response.setFormattedResult(savedResult.getHit() ? "Попадание" : "Промах");

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking point", e);
        }
    }

    @GetMapping("/results")
    public ResponseEntity<List<PointResponseDTO>> getUserResults() {
        try {
            User currentUser = getCurrentUser();
            List<PointResult> results = pointService.getUserResults(currentUser);

            List<PointResponseDTO> response = results.stream()
                    .map(result -> {
                        PointResponseDTO dto = new PointResponseDTO();
                        dto.setX(result.getX());
                        dto.setY(result.getY());
                        dto.setR(result.getR());
                        dto.setHit(result.getHit());
                        dto.setFormattedTime(pointService.formatDateTime(result.getCreatedAt()));
                        dto.setFormattedExecutionTime(pointService.formatExecutionTime(result.getExecutionTime()));
                        dto.setColor(result.getHit() ? "green" : "red");
                        dto.setFormattedResult(result.getHit() ? "Попадание" : "Промах");
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .body(response);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading results", e);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearResults() {
        try {
            User currentUser = getCurrentUser();
            pointService.clearUserResults(currentUser);
            return ResponseEntity.ok()
                    .build();

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error clearing results", e);
        }
    }
}