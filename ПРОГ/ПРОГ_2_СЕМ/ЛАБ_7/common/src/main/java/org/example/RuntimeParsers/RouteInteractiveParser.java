package org.example.RuntimeParsers;

import org.example.exemplars.Coordinates;
import org.example.exemplars.LocationFrom;
import org.example.exemplars.LocationTo;
import org.example.exemplars.Route;
import java.time.ZonedDateTime;

public class RouteInteractiveParser extends RuntimeParser<Route> {

    private final String username;

    public RouteInteractiveParser(String username){
        this.username = username;
    }
    @Override
    public Route parse() {
        return new Route(
                0L,
                this.username,
                askString("название организации", " (строка, поле не может быть пустым)", s -> !s.isEmpty()),
                askCoordinates(),
                ZonedDateTime.now(),
                askLocationFrom(),
                askLocationTo(),
                askDouble("дистанция", " (десятичная дробь, значение должно быть больше 1)", o -> (o != null && o > 1))
        );
    }

    private Coordinates askCoordinates() {
        return new CoordinatesInteractiveParser().parse();
    }

    private LocationFrom askLocationFrom() {
        return new LocationFromInteractiveParser().parse();
    }

    private LocationTo askLocationTo() {
        return new LocationToInteractiveParser().parse();
    }
}
