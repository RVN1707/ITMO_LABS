package org.example.Commands;

import org.example.Managers.CollectionManager;

public class ShowCommand implements CommandInterface {

    private final CollectionManager manager;

    public ShowCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }
        var collection = manager.getCollection();

        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста!");
        } else {
            System.out.println("Элементы коллекции:");
            manager.show();
        }
        return 0;
    }

    @Override
    public String toString() {
        return ": вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
