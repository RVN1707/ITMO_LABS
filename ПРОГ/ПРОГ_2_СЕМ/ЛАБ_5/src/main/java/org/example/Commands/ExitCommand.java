package org.example.Commands;

public class ExitCommand implements CommandInterface {

    @Override
    public int execute(String[] args) {
        if (args.length != 0) {
            System.err.println("Команда не принимает аргументы!");
            return 1;
        }
        System.out.println("Завершаю программу!");
        System.exit(0);
        return 0;
    }

    @Override
    public String toString() {
        return ": завершить программу (без сохранения в файл)";
    }
}
