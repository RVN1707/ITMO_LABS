package org.example.FileParsers;

import org.example.ParserInterface;
import org.example.Utils.MapUtil;
import org.example.exemplars.Coordinates;
import org.example.exemplars.LocationFrom;
import org.example.exemplars.LocationTo;
import org.example.exemplars.Route;
import java.time.ZonedDateTime;

public class FileParserRoute extends FileParser  implements ParserInterface<Route> {

    FileParserCoordinates coordinatesParser;
    FileParserLocationFrom locationFromParser;
    FileParserLocationTo locationToParser;
    MapUtil mapUtil;

    public FileParserRoute(){
        this.coordinatesParser= new FileParserCoordinates();
        this.locationFromParser= new FileParserLocationFrom();
        this.locationToParser= new FileParserLocationTo();
        this.mapUtil = new MapUtil();

    }

    @Override
    public Route parse() {
        Long id = Long.parseLong(map.get("id"));
        String name = map.get("name");
        coordinatesParser.setMap(mapUtil.getSubMapByPrefix(map, "coords_"));
        Coordinates coordinates = coordinatesParser.parse();
        ZonedDateTime creationDate = ZonedDateTime.parse(map.get("cr_date"));
        locationFromParser.setMap(mapUtil.getSubMapByPrefix(map, "loc_from_"));
        LocationFrom locationFrom = locationFromParser.parse();
        locationToParser.setMap(mapUtil.getSubMapByPrefix(map, "loc_to_"));
        LocationTo locationTo = locationToParser.parse();
        Float distance = Float.parseFloat(map.get("distance"));
        return new Route(id, name, coordinates, creationDate, locationFrom, locationTo, distance);
    }
}
