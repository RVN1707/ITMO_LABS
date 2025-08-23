package org.example.Managers;

import org.example.Commands.CommandInterface;
import org.example.Response;
import org.example.exemplars.Route;

import java.util.HashMap;

public class CommandManager {
    HashMap<String, CommandInterface> commands = new HashMap<>();
    public void addCommand(String name, CommandInterface command) {
        commands.put(name, command);
    }

    public HashMap<String, CommandInterface> getCommands() {
        return commands;
    }
    public Response executeCommand(String name, String[] args, Route routeObject) {
        CommandInterface command = commands.get(name);
        if (command != null) {
            try {
                return command.execute(args, routeObject);
            } catch (Exception e) {
                System.err.println(e.getClass().getName());
            }
        }
        System.err.println("Команда не найдена! Попробуйте ввести 'help' для получения списка команд.");
        return null;
    }
}
