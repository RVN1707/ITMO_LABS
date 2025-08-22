package org.example.Parsers.FileParsers;

import org.example.Parsers.ParserInterface;
import org.example.exemplars.LocationFrom;

public class FileParserLocationFrom extends FileParser implements ParserInterface<LocationFrom> {
    @Override
    public LocationFrom parse() {

        if (map.get("x") == "" && map.get("y") == "" && map.get("z") == "") {
            return null;
        }else {
            Integer x = Integer.parseInt(map.get("x"));
            Double y = Double.parseDouble(map.get("y"));
            Float z = Float.parseFloat(map.get("z"));
            return new LocationFrom(x, y, z);
        }
    }
}
