package org.example.Commands;

import org.example.Managers.CollectionManager;
import org.example.Parsers.RuntimeParsers.RouteInteractiveParser;

public class AddCommand implements CommandInterface {

    private final CollectionManager manager;

    public AddCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }
        manager.add(new RouteInteractiveParser(manager).parse());
        return 0;
    }

    @Override
    public String toString() {
        return ": добавить новый элемент в коллекцию";
    }
}
