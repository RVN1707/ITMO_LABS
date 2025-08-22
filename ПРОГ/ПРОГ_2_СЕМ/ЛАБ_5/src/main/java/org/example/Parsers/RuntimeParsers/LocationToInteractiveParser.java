package org.example.Parsers.RuntimeParsers;

import org.example.exemplars.LocationFrom;
import org.example.exemplars.LocationTo;

public class LocationToInteractiveParser extends RuntimeParser<LocationTo> {
    @Override
    public LocationTo parse() {
        return new LocationTo(
                askLong("Локация куда x"," (целое число, поле не может быть пустым)", o -> o!=null),
                askLong("Локация куда y"," (десятичная дробь, поле не может быть пустым)", o -> o!=null),
                askFloat("Локация куда z"," (десятичная дробь, поле не может быть пустым)",o -> o!=null)
        );
    }
}
