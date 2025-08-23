package org.example.FileParsers;

import org.example.ParserInterface;
import org.example.exemplars.Coordinates;

public class FileParserCoordinates extends FileParser implements ParserInterface<Coordinates> {

    @Override
    public Coordinates  parse() throws  NumberFormatException{
            Double x = map.get("x").isEmpty()? null : Double.parseDouble(map.get("x"));
            Float y = Float.parseFloat(map.get("y"));
            return new Coordinates(x,y);
    }
}
