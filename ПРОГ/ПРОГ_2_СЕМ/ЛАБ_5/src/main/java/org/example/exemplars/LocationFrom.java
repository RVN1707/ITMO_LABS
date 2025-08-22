package org.example.exemplars;

public class LocationFrom {
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
