package org.example;

public class ResultData {
    private final Double x;
    private final Double y;
    private final Double r;
    private final Boolean hit;
    private final Long execution_time;
    private final String current_time;
    private final String hit_icon;

    public ResultData(Double x, Double y, Double r, Boolean hit,
                      Long executionTime, String currentTime, String hitIcon) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.execution_time = executionTime;
        this.current_time = currentTime;
        this.hit_icon = hitIcon;
    }
}