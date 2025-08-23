package org.example.exemplars;

import java.io.Serializable;

public class LocationFrom  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer x;
    private Double y; //Поле не может быть null
    private Float z;

    public LocationFrom(Integer x, Double y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Integer getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "LocationFrom{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
