package org.example.Commands;

import org.example.Managers.CollectionManager;

public class InfoCommand implements CommandInterface {

    private final CollectionManager manager;

    public InfoCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }

        System.out.println("Дата инициализации коллекции: " + CollectionManager.initializationTime);
        var collection = manager.getCollection();
        System.out.println("Тип коллекции: " + collection.getClass().getName());
        System.out.println("Размер коллекции: " + collection.size());

        return 0;
    }

    @Override
    public String toString() {
        return ": вывести в стандартный поток вывода информацию о коллекции";
    }
}
