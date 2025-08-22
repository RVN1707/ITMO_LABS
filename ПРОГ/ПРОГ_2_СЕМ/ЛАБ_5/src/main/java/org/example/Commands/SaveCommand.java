package org.example.Commands;

import org.example.Managers.FileManager;

public class SaveCommand implements CommandInterface {

    private final FileManager fileManager;


    public SaveCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }

        try {
            fileManager.saveCollectionToCsv();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 2;
        }

        System.out.println("Коллекция сохранена в файл");
        return 0;
    }

    @Override
    public String toString() {
        return ": сохранить коллекцию в файл";
    }
}
