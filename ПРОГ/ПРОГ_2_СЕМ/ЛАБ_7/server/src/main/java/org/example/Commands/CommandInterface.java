package org.example.Commands;

import org.example.Response;
import org.example.User;
import org.example.exemplars.Route;

public interface CommandInterface {
    Response execute(User user, String[] args, Route routeObject); //throws FileNotFoundException;
}
