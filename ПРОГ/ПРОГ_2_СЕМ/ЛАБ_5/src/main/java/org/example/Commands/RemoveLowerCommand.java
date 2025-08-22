package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Parsers.RuntimeParsers.RouteInteractiveParser;

public class RemoveLowerCommand implements CommandInterface {

    private final CollectionManager manager;

    public RemoveLowerCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }

        var size = manager.getCollection().size();
        manager.removeLower(new RouteInteractiveParser(manager).parse());
        System.out.println("Удалено " + String.valueOf(size - manager.getCollection().size()) + " элементов, меньшие, чем заданный");
        return 0;
    }

    @Override
    public String toString() {
        return ": удалить из коллекции все элементы, меньшие, чем заданный";
    }
}