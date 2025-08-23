package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.exemplars.Route;

public class UpdateIdCommand implements CommandInterface {

    private final CollectionManager manager;

    public UpdateIdCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 1) {
            return new Response("Команда принимает один аргумент!", "");
        }
        if (!args[0].matches("\\d+")) {
            return new Response("id должен быть числом!", " ");
        }
        long id = Long.parseLong(args[0]);
        if (manager.getById(id) == null) {
            return new Response("Элемента с таким id нет в коллекции!", " ");
        }
        manager.update(id, routeObject);
        return new Response("Обновлен элемент с id " + args[0], " ");
    }

    @Override
    public String toString() {
        return " <id> : обновить значение элемента коллекции, id которого равен заданному";
    }
}
