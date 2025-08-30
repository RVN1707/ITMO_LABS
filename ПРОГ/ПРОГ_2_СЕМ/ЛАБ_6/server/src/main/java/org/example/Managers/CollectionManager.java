package org.example.Managers;

import org.example.Response;
import org.example.exemplars.Route;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {

    public static Date initializationTime = new Date();
    LinkedList<Route> collection = new LinkedList<>();

    public void add(Route route){
        collection.add(route);
    }

    public void addIfMin(Route newRoute) {
        if (collection.isEmpty()) {
            collection.add(newRoute);
            return;
        }

        Optional<Route> minRouteOpt = collection.stream()
                .min(Route::compareTo);

        if (minRouteOpt.isPresent() && newRoute.compareTo(minRouteOpt.get()) < 0) {
            collection.add(newRoute);
        }
    }

    public Response show(){
        if (collection.isEmpty()) {
            return new Response("Коллекция пуста!", "");
        } else {
            String elements = getCollection().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));

            return new Response("Элементы коллекции:\n" + elements, " ");
        }
    }

    public void removeGreater(Route o) {
        collection.removeIf(route -> route.compareTo(o) > 0);
    }

    public void removeLower(Route o) {
        collection.removeIf(route -> route.compareTo(o) < 0);
    }

    public Response printFieldAscendingDistance() {

        List<Double> distances = collection.stream()
                .map(Route::getDistance)
                .sorted()
                .toList();

        if (distances.isEmpty()) {
            return new Response("Коллекция пуста.", "");
        } else {
            String distancesStr = distances.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
            return new Response("Значения поля distance в порядке возрастания:\n" + distancesStr, "");
        }
    }

    public Response printFieldDescendingDistance() {
        List<Double> distances = collection.stream()
                .map(Route::getDistance)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (distances.isEmpty()) {
            return new Response("Коллекция пуста.", "");
        } else {
            String distancesStr = distances.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"));
            return new Response("Значения поля distance в порядке убывания:\n" + distancesStr, "");
        }
    }

    public void clear() {
        initializationTime = new Date();
        collection.clear();
    }

    public Route getById(Long id) {
        return collection.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }

    public List<Route> getCollection() {
        return collection;
    }

    public void update(long id, Route o) {
        collection.stream().filter(route -> route.getId() == id).forEach(route -> {
            route.setName(o.getName());
            route.setCoordinates(o.getCoordinates());
            route.setCreationDate(o.getCreationDate());
            route.setFrom(o.getFrom());
            route.setTo(o.getTo());
            route.setDistance(o.getDistance());
        });
    }

    public void removeById(Long id) {
        collection.removeIf(o -> o.getId().equals(id));
    }

    public Response filterContainsName(String name){
        var filteredRoute = collection.stream().filter(o -> o.getName().contains(name)).toList();
        if (!filteredRoute.isEmpty()) {
            StringBuilder response = new StringBuilder();
            response.append("Элементы, значение поля name которых содержит заданную подстроку:").append("\n");
            filteredRoute.stream().map(response::append).forEach(s -> response.append("\n"));
            return new Response(response.toString(), " ");
        } else {
            return new Response("Ничего не найдено!", "");
        }
    }
}
