package org.example.Commands;

import java.io.FileNotFoundException;

public interface CommandInterface {
    int execute(String[] args) throws FileNotFoundException;
}
