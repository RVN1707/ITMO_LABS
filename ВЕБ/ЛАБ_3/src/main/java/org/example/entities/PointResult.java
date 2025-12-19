package org.example.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "point_results")
public class PointResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private Double r;

    @Column(nullable = false)
    private Boolean result;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "execution_time")
    private Long executionTime;

    public PointResult() {
        this.timestamp = LocalDateTime.now();
    }

    public PointResult(Double x, Double y, Double r, Boolean result, Long executionTime) {
        this();
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
        this.executionTime = executionTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }

    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }

    public Double getR() { return r; }
    public void setR(Double r) { this.r = r; }

    public Boolean getResult() { return result; }
    public void setResult(Boolean result) { this.result = result; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getExecutionTime() { return executionTime; }
    public void setExecutionTime(Long executionTime) { this.executionTime = executionTime; }

    public String getFormattedResult() {
        return result ? "Попадание" : "Промах";
    }

    public String getColor() {
        return result ? "green" : "red";
    }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    public String getFormattedExecutionTime() {
        return executionTime + " мкс";
    }

    // Метод для получения значения X для графика (округленное)
    public Double getGraphX() {
        return x;
    }

    // Метод для получения значения Y для графика (округленное)
    public Double getGraphY() {
        return y;
    }

    // Метод для проверки, является ли точка "попаданием"
    public boolean isHit() {
        return Boolean.TRUE.equals(result);
    }

    @Override
    public String toString() {
        return "PointResult{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", result=" + result +
                ", timestamp=" + getFormattedTime() +
                ", executionTime=" + executionTime +
                '}';
    }
}