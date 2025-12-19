package org.example;

public class PointRequest {
    private final Double x;
    private final Double y;
    private final Double r;

    public PointRequest(Double x, Double y, Double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    // Геттеры для доступа к координатам
    public Double getX() { return x; }
    public Double getY() { return y; }
    public Double getR() { return r; }
}
