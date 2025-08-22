package org.example.exemplars;

public class Coordinates {
    private Double x;
    private Float y; //Поле не может быть null
    public Coordinates(Double x, Float y){
        this.x=x;
        this.y=y;
    }

    public Double getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
