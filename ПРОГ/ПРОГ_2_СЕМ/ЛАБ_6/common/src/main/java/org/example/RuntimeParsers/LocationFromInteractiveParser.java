package org.example.RuntimeParsers;

import org.example.exemplars.LocationFrom;

public class LocationFromInteractiveParser extends RuntimeParser<LocationFrom> {
    @Override
    public LocationFrom parse() {
        while (true){
            Integer x = askInteger("Локация откуда x"," (целое число)", o -> true);
            Double y = askDouble("Локация откуда y"," (десятичная дробь)", o -> true);
            Float z = askFloat("Локация откуда z"," (десятичная дробь)",o -> true);
            if (x == null && y == null && z == null){
                return null;
            }else if(x != null && y != null && z != null) {
                return new LocationFrom(x, y, z);
            }else{
                System.out.println("Значения могут быть либо одновременно пустыми или не пустыми. Попробуйте еще раз");
            }
        }

    }
}
