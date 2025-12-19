package org.example;

public class BaseRequest {
    private Double x;
    private Double y;
    private Double r;
    private String action;

    // Геттеры для доступа к полям (используются Jackson)
    public Double getX() { return x; }
    public Double getY() { return y; }
    public Double getR() { return r; }
    public String getAction() { return action; }
}