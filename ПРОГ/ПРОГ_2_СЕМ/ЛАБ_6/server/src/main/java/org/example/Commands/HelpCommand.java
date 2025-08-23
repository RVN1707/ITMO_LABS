package org.example.Commands;

import org.example.Managers.CommandManager;
import org.example.Response;
import org.example.exemplars.Route;

public class HelpCommand implements CommandInterface {
    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }
    @Override
    public Response execute(String[] args, Route routeObject) {
        if (args.length != 0) {
            return new Response("Команда не принимает аргументы!", "");
        }

        StringBuilder help = new StringBuilder();

        help.append("Доступные команды:\n");
        manager.getCommands().forEach((name, command) -> help.append(name + command.toString()+'\n'));
        return new Response(help.toString(), "");
    }

    @Override
    public String toString() {
        return ": вывести справку по доступным командам";
    }
}
