package org.example.Parsers.FileParsers;

import org.example.Parsers.ParserInterface;
import org.example.exemplars.LocationTo;

public class FileParserLocationTo extends FileParser implements ParserInterface<LocationTo> {

    @Override
    public LocationTo parse() {
        Long x = map.get("x").isEmpty() ? null : Long.parseLong(map.get("x"));
        Long y = map.get("y").isEmpty() ? null : Long.parseLong(map.get("y"));
        Float z = Float.parseFloat(map.get("z"));
        return new LocationTo(x, y, z);
    }

}
