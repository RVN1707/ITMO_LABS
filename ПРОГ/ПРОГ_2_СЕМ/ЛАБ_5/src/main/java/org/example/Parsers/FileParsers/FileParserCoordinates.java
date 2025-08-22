package org.example.Parsers.FileParsers;

import org.example.Parsers.ParserInterface;
import org.example.exemplars.Coordinates;
import java.util.Map;

public class FileParserCoordinates extends FileParser implements ParserInterface<Coordinates> {
    public Map<String, String> map;
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    @Override
    public Coordinates  parse() throws  NumberFormatException{
            Double x = map.get("x").isEmpty()? null : Double.parseDouble(map.get("x"));
            Float y = Float.parseFloat(map.get("y"));
            return new Coordinates(x,y);
    }

}
