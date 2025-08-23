package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.exemplars.Route;

public class InfoCommand implements CommandInterface {

    private final CollectionManager manager;

    public InfoCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", "");
        }

        StringBuilder response = new StringBuilder();
        response.append("Дата инициализации коллекции: ").append(CollectionManager.initializationTime).append("\n");
        response.append("Тип коллекции: ").append(manager.getCollection().getClass().getName()).append("\n");
        response.append("Размер коллекции: ").append(manager.getCollection().size());
        return new Response(response.toString(), " ");


    }

    @Override
    public String toString() {
        return ": вывести в стандартный поток вывода информацию о коллекции";
    }
}
