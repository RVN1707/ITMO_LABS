package org.example.RuntimeParsers;

import org.example.exemplars.Coordinates;

public class CoordinatesInteractiveParser extends RuntimeParser<Coordinates> {

    @Override
    public Coordinates parse() {
        return new Coordinates(
                askDouble("координата x", " (десятичная дробь)", o -> true),
                askFloat("координата y", " (десятичная дробь, поле не может быть пустым)", o -> o!=null)
        );
    }
}
