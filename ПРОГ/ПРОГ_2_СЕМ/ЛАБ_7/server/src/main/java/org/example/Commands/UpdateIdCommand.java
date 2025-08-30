package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public class UpdateIdCommand implements CommandInterface {

    private final CollectionManager manager;

    public UpdateIdCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(User user, String[] args, Route routeObject) {
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
        if (manager.update(id, routeObject, user)){
            return new Response("Обновлен элемент с id " + args[0], " ");
        }else{
            return new Response("Не удалось изменить элемент с id " + args[0], " ");
        }
    }

    @Override
    public String toString() {
        return " <id> : обновить значение элемента коллекции, id которого равен заданному";
    }
}
