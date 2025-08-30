package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public class PrintFieldDescendingDistanceCommand implements CommandInterface{

    private final CollectionManager manager;

    public PrintFieldDescendingDistanceCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(User user, String[] args, Route routeObject) {
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
