package org.example.Commands;

import org.example.Managers.CommandManager;

public class HelpCommand implements CommandInterface {
    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }
    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }

        System.out.println("Доступные команды:");
        manager.getCommands().forEach((name, command) -> System.out.println(name + command.toString()));
        return 0;
    }

    @Override
    public String toString() {
        return ": вывести справку по доступным командам";
    }
}
