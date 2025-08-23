package org.example.Managers;

import org.example.exemplars.Route;

public class ValidationManager {
    public boolean isValidRoute(Route route, CollectionManager collectionManager){
        return route.getId() > 0 &&
                collectionManager.getById(route.getId()) == null &&
                route.getName() != null &&
                !route.getName().isEmpty() &&
                route.getDistance() > 1 ;
    }
}
