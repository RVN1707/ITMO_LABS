package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.exemplars.Route;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PrintFieldDescendingDistanceCommand implements CommandInterface{

    private final CollectionManager manager;

    public PrintFieldDescendingDistanceCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", "");
        }else {
            return manager.printFieldDescendingDistance();
        }
    }

    @Override
    public String toString() {
        return ": вывести значения поля distance всех элементов в порядке возрастания";
    }
}
