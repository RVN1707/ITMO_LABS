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
}
