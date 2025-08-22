package org.example.Commands;

import org.example.Managers.CollectionManager;

public class FilterContainsNameCommand implements CommandInterface {

    private final CollectionManager manager;

    public FilterContainsNameCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 1) {
            System.err.println("Команда принимает один аргумент!");
            return 1;
        }
        var filteredRoute = manager.filterContainsName(args[0]);
        if (!filteredRoute.isEmpty()) {
            System.out.println("Элементы, значение поля name которых содержит заданную подстроку:");
            filteredRoute.stream().map(Object::toString).forEach(System.out::println);
        } else {
            System.out.println("Ничего не найдено! :(");
        }

        return 0;
    }

    @Override
    public String toString() {
        return " <name> : вывести элементы, значение поля name которых содержит заданную подстроку";
    }
}
