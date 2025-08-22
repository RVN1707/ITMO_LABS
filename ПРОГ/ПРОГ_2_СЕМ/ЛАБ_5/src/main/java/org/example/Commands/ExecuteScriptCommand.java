package org.example.Commands;

import org.example.Managers.CommandManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScriptCommand implements CommandInterface {

    private final CommandManager commandManager;
    private final Set<Path> executingScripts = new HashSet<>();

    public ExecuteScriptCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 1) {
            System.err.println("Ошибка: требуется 1 аргумент - путь к скрипту");
            return 1;
        }
        String fullPath = String.join(" ", args);

        if (fullPath.startsWith("\"") && fullPath.endsWith("\"")) {
            fullPath = fullPath.substring(1, fullPath.length() - 1);
        }
        try {
            Path scriptPath = Paths.get(fullPath).toAbsolutePath().normalize();
            File scriptFile = scriptPath.toFile();

            synchronized (executingScripts) {
                if (executingScripts.contains(scriptPath)) {
                    System.err.println("Ошибка: рекурсивный вызов скрипта " + scriptPath);
                    return 1;
                }
                executingScripts.add(scriptPath);
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(scriptFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

                    try {
                        String[] parts = line.split("\\s+", 2);
                        String commandName = parts[0].toLowerCase();
                        String[] commandArgs = parts.length > 1 ?
                                parts[1].split("\\s+") : new String[0];

                        commandManager.executeCommand(commandName, commandArgs);
                    } catch (Exception e) {
                        System.err.println("Ошибка выполнения команды: " + e.getMessage());
                    }
                }
            } finally {
                synchronized (executingScripts) {
                    executingScripts.remove(scriptPath);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка работы со скриптом: " + e.getMessage());
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return ": выполнить скрипт находящийся в файле по указанному адресу";
    }
}