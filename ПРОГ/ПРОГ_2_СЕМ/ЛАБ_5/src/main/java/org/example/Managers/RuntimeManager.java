package org.example.Managers;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RuntimeManager {
    private CommandManager commandManager;
    private FileManager fileManager;
public RuntimeManager(CommandManager commandManager, FileManager fileManager){
    this.commandManager=commandManager;
    this.fileManager=fileManager;
}
    public void interactiveMode() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        fileManager.readCSVFile();
        while (true) {
            try {
                System.out.print(">>> ");
                String[] userCommand = scanner.nextLine().trim().split(" ");
                commandManager.executeCommand(userCommand[0].toLowerCase(), Arrays.copyOfRange(userCommand, 1, userCommand.length));
            } catch (NoSuchElementException e) {
                System.out.println("\n[Ввод завершён пользователем (CTRL+D). Выход из программы.]");
                break;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}