package org.example.exemplars;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
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
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
