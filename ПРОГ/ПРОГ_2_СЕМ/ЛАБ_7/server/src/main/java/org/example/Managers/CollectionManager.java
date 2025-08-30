package org.example.Managers;

import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

import java.sql.Array;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CollectionManager {

    private final ReadWriteLock locker = new ReentrantReadWriteLock();
    public List<Route> getCollection() {
        return collection;
    }
    public static Date initializationTime = new Date();
    ArrayList<Route> collection = new ArrayList<>();

    public void add(Route route, boolean flag){
        locker.writeLock().lock();
        try {
            if (flag) {
                Long id = DBManager.addRoute(route);
                if (id != -1) {
                    route.setId(id);
                    collection.add(route);
                } else {
                    System.out.println("Error adding organization to the database.");
                }
            } else {
                collection.add(route);
            }
        } finally {
            locker.writeLock().unlock();
        }
    }

    public void addIfMin(Route newRoute) {
        locker.writeLock().lock();
        if (collection.isEmpty()) {
            collection.add(newRoute);
            return;
        }

        Optional<Route> minRouteOpt = collection.stream()
                .min(Route::compareTo);

        if (minRouteOpt.isPresent() && newRoute.compareTo(minRouteOpt.get()) < 0) {
            if (DBManager.addRoute(newRoute) != -1) {
                collection.add(newRoute);
            }
        }
        locker.writeLock().unlock();
    }

    public void removeGreater(Route o) {
        locker.writeLock().lock();
        try{
            DBManager.removeRouteGreater(o);
            collection.removeIf(route -> route.compareTo(o) > 0 && Objects.equals(route.getUsername(), o.getUsername()));
        }finally {
            locker.writeLock().unlock();
        }
    }

    public void removeLower(Route o) {
        locker.writeLock().lock();
        try{
            DBManager.removeRouteLower(o);
            collection.removeIf(route -> route.compareTo(o) < 0 && Objects.equals(route.getUsername(), o.getUsername()));
        }finally {
            locker.writeLock().unlock();
        }
    }

    public void clear(User user) {
        locker.writeLock().lock();
        try {
            if (DBManager.clearRoute(user)) {
                collection.removeIf(route -> route.getUsername().equals(user.getUsername()));
                DBManager.load(this);
            }
            initializationTime = new Date();
        } finally {
            locker.writeLock().unlock();
        }
    }

    public Route getById(Long id) {
        return collection.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }

    public boolean update(Long id, Route o, User user) {
        if (!getById(id).getUsername().equals(user.getUsername())) {
            return false;
        }
        locker.writeLock().lock();
        try {
            if (DBManager.updateRoute(id, o, user)) {
                collection.stream().filter(route -> route.getId() == id).forEach(route -> {
                    route.setName(o.getName());
                    route.setCoordinates(o.getCoordinates());
                    route.setCreationDate(o.getCreationDate());
                    route.setFrom(o.getFrom());
                    route.setTo(o.getTo());
                    route.setDistance(o.getDistance());
                });
                return true;
            }else {
                return false;
            }

        } finally {
            locker.writeLock().unlock();
        }
    }

    public void removeById(User user, Long id) {
        locker.writeLock().lock();
        try{
            if(DBManager.removeRouteById(user.getUsername(), id)){
                collection.removeIf(o -> o.getId().equals(id));
            }
        }finally {
            locker.writeLock().unlock();
        }
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
}
