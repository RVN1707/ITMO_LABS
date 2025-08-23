package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.exemplars.Route;

public class ShowCommand implements CommandInterface {

    private final CollectionManager manager;

    public ShowCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", "");
        }
        return manager.show();
    }

    @Override
    public String toString() {
        return ": вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
