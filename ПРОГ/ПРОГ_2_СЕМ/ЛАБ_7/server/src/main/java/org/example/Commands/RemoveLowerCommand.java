package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public class RemoveLowerCommand implements CommandInterface {

    private final CollectionManager manager;

    public RemoveLowerCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(User user, String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", " ");
        }

        var size = manager.getCollection().size();
        manager.removeLower(routeObject);
        return new Response("Удалено " + (size - manager.getCollection().size()) + " элементов, меньших, чем заданный", " ");
    }

    @Override
    public String toString() {
        return ": удалить из коллекции все элементы, меньшие, чем заданный";
    }
}