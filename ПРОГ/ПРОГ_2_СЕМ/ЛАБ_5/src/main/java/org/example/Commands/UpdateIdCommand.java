package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Parsers.RuntimeParsers.RouteInteractiveParser;

public class UpdateIdCommand implements CommandInterface {

    private final CollectionManager manager;

    public UpdateIdCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 1) {
            System.err.println("Команда принимает один аргумент!");
            return 1;
        }
        if (!args[0].matches("\\d+")) {
            System.err.println("id должен быть числом!");
            return 2;
        }
        long id = Long.parseLong(args[0]);
        if (manager.getById(id) == null) {
            System.err.println("Элемента с таким id нет в коллекции!");
            return 3;
        }
        manager.update(id, new RouteInteractiveParser(manager).parse());
        System.out.println("Обновлен элемент с id " + args[0]);
        return 0;
    }

    @Override
    public String toString() {
        return " <id> : обновить значение элемента коллекции, id которого равен заданному";
    }
}
