package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Response;
import org.example.exemplars.Route;

public class FilterContainsNameCommand implements CommandInterface {

    private final CollectionManager manager;

    public FilterContainsNameCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 1) {
            return new Response("Команда принимает один аргумент!", "");
        }
        var filteredRoute = manager.filterContainsName(args[0]);
        if (!filteredRoute.isEmpty()) {
            StringBuilder response = new StringBuilder();
            response.append("Элементы, значение поля name которых содержит заданную подстроку:").append("\n");
            filteredRoute.stream().map(response::append).forEach(s -> response.append("\n"));
            return new Response(response.toString(), " ");
        } else {
            return new Response("Ничего не найдено! :(", "");
        }
    }

    @Override
    public String toString() {
        return " <name> : вывести элементы, значение поля name которых содержит заданную подстроку";
    }
}
