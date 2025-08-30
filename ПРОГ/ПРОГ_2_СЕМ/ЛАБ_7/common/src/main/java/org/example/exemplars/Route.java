package org.example.exemplars;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class Route implements Comparable<Route>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String username;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private LocationFrom from; //Поле может быть null
    private LocationTo to; //Поле не может быть null
    private double distance; //Поле может быть null, Значение поля должно быть больше 1

    public Route(Long id, String username, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, LocationFrom from, LocationTo to, double distance) {
        this.id= id;
        this.username = username;
        this.name=name;
        this.coordinates=coordinates;
        this.creationDate=creationDate;
        this.from=from;
        this.to=to;
        this.distance=distance;
    }

    public Long getId() {
        return id;
    }

    public LocationFrom getFrom() {
        return from;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocationTo getTo() {
        return to;
    }

    public void setTo(LocationTo to) {
        this.to = to;
    }

    public void setFrom(LocationFrom from) {
        this.from = from;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int compareTo(Route o) {
        return Comparator.comparingDouble(Route::getDistance)
                .thenComparingLong(Route::getId)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", username=" + username +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }
}