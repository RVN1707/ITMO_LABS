package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Parsers.RuntimeParsers.RouteInteractiveParser;

public class RemoveGreaterCommand implements CommandInterface{

    private final CollectionManager manager;

    public RemoveGreaterCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }
        var size = manager.getCollection().size();
        manager.removeGreater(new RouteInteractiveParser(manager).parse());
        System.out.println("Удалено " + String.valueOf(size - manager.getCollection().size()) + " элементов, превышающих заданный");
        return 0;
    }

    @Override
    public String toString() {
        return ": удалить из коллекции все элементы, превышающие заданный";
    }
}
