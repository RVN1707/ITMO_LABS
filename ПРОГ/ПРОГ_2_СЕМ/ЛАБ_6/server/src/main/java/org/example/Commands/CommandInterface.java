package org.example.Commands;

import org.example.Response;
import org.example.exemplars.Route;

import java.io.FileNotFoundException;

public interface CommandInterface {
    Response execute(String[] args, Route routeObject) throws FileNotFoundException;
}
