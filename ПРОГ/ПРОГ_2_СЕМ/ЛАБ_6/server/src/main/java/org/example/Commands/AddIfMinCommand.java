package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Managers.IdManager;
import org.example.Response;
import org.example.exemplars.Route;

public class AddIfMinCommand implements CommandInterface{
    CollectionManager manager;

    public AddIfMinCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", "");
        }
        IdManager idManager = new IdManager(manager);
        var size = manager.getCollection().size();

        routeObject.setId(idManager.generateId());
        manager.addIfMin(routeObject);

        if (size == manager.getCollection().size())
            return new Response("Элемент не добавлен", "");
        else{
            return new Response("", "");
        }
    }

    @Override
    public String toString() {
        return ": добавить новый элемент, если его значение меньше, чем у наименьшего элемента коллекции";
    }
}
