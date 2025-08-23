package org.example.Commands;
import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.exemplars.Route;

public class RemoveByIdCommand implements CommandInterface {
    CollectionManager manager;

    public RemoveByIdCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 1) {
            return new Response("Команда принимает один аргумент!", " ");
        }

        try {
            long id = Long.parseLong(args[0]);
            if (manager.getById(id) == null) {
                return new Response("Элемент с id " + id + " не найден", " ");
            }

            manager.removeById(id);
            return new Response("Элемент с id " + id + " удален", " ");
        } catch (NumberFormatException e) {
            return new Response("Неверный формат аргумента!", " ");
        }
    }

    @Override
    public String toString() {
        return " <id> : удалить элемент из коллекции по его id";
    }
}