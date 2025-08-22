package org.example.Commands;

import org.example.Managers.CollectionManager;

public class PrintFieldAscendingDistanceCommand implements CommandInterface{

    private final CollectionManager manager;

    public PrintFieldAscendingDistanceCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }else {
            manager.printFieldAscendingDistance();
            return 0;
        }
    }

    @Override
    public String toString() {
        return ": вывести значения поля distance всех элементов в порядке возрастания";
    }
}

