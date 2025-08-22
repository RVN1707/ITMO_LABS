package org.example.Commands;

import org.example.Managers.CollectionManager;

public class PrintFieldDescendingDistanceCommand implements CommandInterface{

    private final CollectionManager manager;

    public PrintFieldDescendingDistanceCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }else {
            manager.printFieldDescendingDistance();
            return 0;
        }
    }

    @Override
    public String toString() {
        return ": вывести значения поля distance всех элементов в порядке возрастания";
    }
}
