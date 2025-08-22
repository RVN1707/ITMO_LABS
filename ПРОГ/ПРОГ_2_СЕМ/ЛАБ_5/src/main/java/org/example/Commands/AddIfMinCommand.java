package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Parsers.RuntimeParsers.RouteInteractiveParser;

public class AddIfMinCommand implements CommandInterface{
    CollectionManager manager;

    public AddIfMinCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }
        var size = manager.getCollection().size();
        manager.addIfMin(new RouteInteractiveParser(manager).parse());

        if (size == manager.getCollection().size())
            System.err.println("Элемент не добавлен");
        return 0;
    }

    @Override
    public String toString() {
        return ": добавить новый элемент, если его значение меньше, чем у наименьшего элемента коллекции";
    }
}
