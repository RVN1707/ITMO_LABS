package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public class FilterContainsNameCommand implements CommandInterface {

    private final CollectionManager manager;

    public FilterContainsNameCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(User user, String[] args, Route routeObject) {
        if (args.length != 1) {
            return new Response("Команда принимает один аргумент!", "");
        }else{
            return manager.filterContainsName(args[0]);
        }
    }

    @Override
    public String toString() {
        return " <name> : вывести элементы, значение поля name которых содержит заданную подстроку";
    }
}
