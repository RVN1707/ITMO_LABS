package org.example.Commands;
import org.example.Managers.CollectionManager;

public class RemoveByIdCommand implements CommandInterface {
    CollectionManager manager;

    public RemoveByIdCommand(CollectionManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(String[] args) {
        if (args.length != 1) {
            System.err.println("Команда принимает один аргумент!");
            return 1;
        }

        try {
            long id = Long.parseLong(args[0]);
            if (manager.getById(id) == null) {
                System.err.println("Элемент с id " + id + " не найден");
                return 2;
            }

            manager.removeById(id);
            System.out.println("Элемент с id " + id + " удален");
            return 0;
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат аргумента!");
            return 3;
        }
    }

    @Override
    public String toString() {
        return " <id> : удалить элемент из коллекции по его id";
    }
}