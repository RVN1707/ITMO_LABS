package org.example.Managers;

public class IdManager {
    private long id = 1;
    private final CollectionManager collectionManager;

    public IdManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public long generateId() {
        while (collectionManager.getById(id) != null) {
            id++;
        }
        return id;
    }
}