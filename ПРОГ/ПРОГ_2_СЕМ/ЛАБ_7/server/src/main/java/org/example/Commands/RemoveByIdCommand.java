package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public class RemoveByIdCommand implements CommandInterface {
    CollectionManager manager;

    public RemoveByIdCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(User user, String[] args, Route routeObject) {
        if (args.length != 1) {
            return new Response("Команда принимает один аргумент!", " ");
        }

        try {
            long id = Long.parseLong(args[0]);
            if (manager.getById(id) == null) {
                return new Response("Элемент с id " + id + " не найден", " ");
            }
            int firstSize = manager.getCollection().size();
            manager.removeById(user, id);
            if (firstSize!=manager.getCollection().size()){
                return new Response("Элемент с id " + id + " удален", " ");
            }else{
                return new Response("Элемент с id "+ id + "не удален, так как не существует или не принадлежит вам", "");
            }

        } catch (NumberFormatException e) {
            return new Response("Неверный формат аргумента!", " ");
        }
    }

    @Override
    public String toString() {
        return " <id> : удалить элемент из коллекции по его id";
    }
}