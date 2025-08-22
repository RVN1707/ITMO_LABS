package org.example.Managers;

import org.example.Commands.CommandInterface;
import java.util.HashMap;

public class CommandManager {
    HashMap<String, CommandInterface> commands = new HashMap<>();
    public void addCommand(String name, CommandInterface command) {
        commands.put(name, command);
    }

    public HashMap<String, CommandInterface> getCommands() {
        return commands;
    }
    public int executeCommand(String name, String[] args) {
        System.out.println(name);
        CommandInterface command = commands.get(name);
        if (command != null) {
            try {
                return command.execute(args);
            } catch (Exception e) {
                System.err.println(e.getClass().getName());
                return -1;
            }
        }
        System.err.println("Команда не найдена! Попробуйте ввести 'help' для получения списка команд.");
        return -1;
    }
}
