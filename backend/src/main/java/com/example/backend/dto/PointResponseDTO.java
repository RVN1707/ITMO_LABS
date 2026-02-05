package com.example.backend.dto;

import lombok.Data;

@Data
public class PointResponseDTO {
    private Double x;
    private Double y;
    private Double r;
    private Boolean hit;
    private String formattedTime;
    private String formattedExecutionTime;
    private String color;
    private String formattedResult;
}