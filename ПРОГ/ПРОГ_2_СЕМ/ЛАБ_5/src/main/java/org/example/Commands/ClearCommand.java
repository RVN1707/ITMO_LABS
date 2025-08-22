package org.example.Commands;

import org.example.Managers.CollectionManager;

public class ClearCommand implements CommandInterface {

    CollectionManager manager;

    public ClearCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }

        System.out.println("Коллекция очищена");
        manager.clear();

        return 0;
    }

    @Override
    public String toString() {
        return ": очистить коллекцию";
    }
}
