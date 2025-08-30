package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public class AddCommand implements CommandInterface {

    private final CollectionManager manager;

    public AddCommand(CollectionManager manager) {
        this.manager = manager;

    }

    @Override
    public Response execute(User user, String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", "");

        }

        manager.add(routeObject, true);
        return new Response("Элемент добален!", "");
    }

    @Override
    public String toString() {
        return ": добавить новый элемент в коллекцию";
    }
}
