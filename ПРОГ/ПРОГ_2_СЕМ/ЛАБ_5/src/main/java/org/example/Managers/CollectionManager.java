package org.example.Managers;

import org.example.exemplars.Route;
import java.util.*;

public class CollectionManager {

    private final IdManager idManager;
    public CollectionManager() {
        this.idManager = new IdManager(this);
    }

    public IdManager getIdManager() {
        return idManager;
    }

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


    public void show(){
        for (int i = 0; i <collection.size(); i++) {
            System.out.println(collection.get(i));
        }
    }

    public void removeGreater(Route o) {
        collection.removeIf(route -> route.compareTo(o) > 0);
    }

    public void removeLower(Route o) {
        collection.removeIf(route -> route.compareTo(o) < 0);
    }

    public List<Route> filterContainsName(String name) {
        return collection.stream().filter(o -> o.getName().contains(name)).toList();
    }

    public void printFieldAscendingDistance() {

        List<Double> distances = collection.stream()
                .map(Route::getDistance)
                .sorted()
                .toList();

        if (distances.isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            System.out.println("Значения поля distance в порядке возрастания:");
            distances.forEach(System.out::println);
        }
    }

    public void printFieldDescendingDistance() {
        List<Double> distances = collection.stream()
                .map(Route::getDistance)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (distances.isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            System.out.println("Значения поля distance в порядке убывания:");
            distances.forEach(System.out::println);
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
}
