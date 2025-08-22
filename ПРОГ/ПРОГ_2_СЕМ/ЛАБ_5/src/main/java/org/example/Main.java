package org.example;

import org.example.Commands.*;
import org.example.Managers.*;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        CollectionManager collectionManager = new CollectionManager();
        FileManager fileManager = new FileManager("LAB", collectionManager);
        CommandManager commandManager = new CommandManager();

        commandManager.addCommand("add", new AddCommand(collectionManager));
        commandManager.addCommand("addifmin", new AddIfMinCommand(collectionManager));
        commandManager.addCommand("clear", new ClearCommand(collectionManager));
        commandManager.addCommand("executescript", new ExecuteScriptCommand(commandManager));
        commandManager.addCommand("exit", new ExitCommand());
        commandManager.addCommand("filtercontainsname", new FilterContainsNameCommand(collectionManager));
        commandManager.addCommand("help", new HelpCommand(commandManager));
        commandManager.addCommand("info", new InfoCommand(collectionManager));
        commandManager.addCommand("printfieldascendingdistance", new PrintFieldAscendingDistanceCommand(collectionManager));
        commandManager.addCommand("printfielddescendingdistance", new PrintFieldDescendingDistanceCommand(collectionManager));
        commandManager.addCommand("removebyid", new RemoveByIdCommand(collectionManager));
        commandManager.addCommand("removegreater", new RemoveGreaterCommand(collectionManager));
        commandManager.addCommand("removelower", new RemoveLowerCommand(collectionManager));
        commandManager.addCommand("save", new SaveCommand(fileManager));
        commandManager.addCommand("show", new ShowCommand(collectionManager));
        commandManager.addCommand("updateId", new UpdateIdCommand(collectionManager));

        new RuntimeManager(commandManager, fileManager).interactiveMode();
    }
}
