package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public class ClearCommand implements CommandInterface {

    CollectionManager manager;

    public ClearCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(User user, String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", "");

        }
        manager.clear(user);
        return new Response("Коллекция очищена", "");
    }

    @Override
    public String toString() {
        return ": очистить коллекцию";
    }
}
