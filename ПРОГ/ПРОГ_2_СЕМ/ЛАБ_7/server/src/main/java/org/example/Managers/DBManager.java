package org.example.Managers;

import org.example.exemplars.LocationFrom;
import org.example.exemplars.Coordinates;
import org.example.exemplars.LocationTo;
import org.example.exemplars.Route;
import java.time.OffsetDateTime;
import org.example.User;
import java.sql.*;

public class DBManager {
    private static Connection connection;

    public static void establishConnection(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.out.println("Ошибка подключения к БД");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkUserExistence(String username) {

        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)";

        try (PreparedStatement p = connection.prepareStatement(query)) {

            p.setString(1, username);
            ResultSet res = p.executeQuery();
            if (res.next()) {
                return res.getBoolean(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean checkUserPassword(User user) {
        var username = user.getUsername();
        var hashedPassword = user.getPassword();

        String query = "SELECT password FROM users WHERE username = ?";

        try (PreparedStatement p = connection.prepareStatement(query)) {

            p.setString(1, username);
            ResultSet res = p.executeQuery();

            if (res.next()) {
                String storedHashedPassword = res.getString("password");
                return storedHashedPassword.equals(hashedPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void addUser(User user) {
        var username = user.getUsername();
        var hashedPassword = user.getPassword();

        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement p = connection.prepareStatement(query)) {

            p.setString(1, username);
            p.setString(2, hashedPassword);
            p.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load(CollectionManager collectionManager) {
        String query = "select id, username, name, coords_x, coords_y, cr_date, loc_from_x, loc_from_y, loc_from_z, loc_to_x, loc_to_y, loc_to_z, distance from route";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            ResultSet res = p.executeQuery();
            while (res.next()) {
                try {
                    Route element = new Route(
                            res.getLong(1),
                            res.getString(2),
                            res.getString(3),
                            new Coordinates(res.getDouble(4), res.getFloat(5)),
                            res.getObject(6, OffsetDateTime.class).toZonedDateTime(),
                            new LocationFrom(res.getInt(7), res.getDouble(8), res.getFloat(9)),
                            new LocationTo(res.getLong(10), res.getLong(11), res.getFloat(12)),
                            res.getDouble(13)
                    );
                    collectionManager.add(element, false);
                } catch (IllegalArgumentException e) {
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long addRoute(Route route) {
        String query = "INSERT INTO route (username, name, coords_x, coords_y, cr_date, loc_from_x, loc_from_y, loc_from_z, loc_to_x, loc_to_y, loc_to_z, distance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, route.getUsername());
            p.setString(2, route.getName());
            p.setDouble(3, route.getCoordinates().getX());
            p.setFloat(4, route.getCoordinates().getY());
            p.setTimestamp(5, Timestamp.from(route.getCreationDate().toInstant()));;
            p.setInt(6, route.getFrom().getX());
            p.setDouble(7, route.getFrom().getY());
            p.setFloat(8, route.getFrom().getZ());
            p.setLong(9, route.getTo().getX());
            p.setLong(10, route.getTo().getY());
            p.setFloat(11, route.getTo().getZ());
            p.setDouble(12, route.getDistance());

            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println("Че то не так");
            return -1L;
        }
        return -1L;
    }

    public static boolean updateRoute(long id, Route route, User user) {
        String query = "UPDATE route SET name = ?, coords_x = ?, coords_y = ?, cr_date = ?, loc_from_x = ?, loc_from_y = ?, loc_from_z = ?, loc_to_x = ?, loc_to_y = ?, loc_to_z = ?, distance = ? where id = ? AND username = ?";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, route.getName());
            p.setDouble(2, route.getCoordinates().getX());
            p.setFloat(3, route.getCoordinates().getY());
            p.setTimestamp(4, Timestamp.from(route.getCreationDate().toInstant()));
            p.setInt(5, route.getFrom().getX());
            p.setDouble(6, route.getFrom().getY());
            p.setFloat(7, route.getFrom().getZ());
            p.setLong(8, route.getTo().getX());
            p.setLong(9, route.getTo().getY());
            p.setFloat(10, route.getTo().getZ());
            p.setDouble(11, route.getDistance());
            p.setLong(12, id);
            p.setString(13, user.getUsername());

            int affectedRows = p.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeRouteById(String username, long id) {
        String query = "DELETE FROM route WHERE id = ? and username = ?";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setLong(1, id);
            p.setString(2, username);
            int affectedRows = p.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean clearRoute(User user) {
        String query = "DELETE FROM route WHERE username = ?";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getUsername());
            int affectedRows = p.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeRouteGreater(Route o) {
        String query = "DELETE FROM route WHERE distance > ? AND username = ?";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setDouble(1, o.getDistance());
            p.setString(2, o.getUsername());
            int affectedRows = p.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeRouteLower(Route o) {
        String query = "DELETE FROM route WHERE distance < ? AND username = ?";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setDouble(1, o.getDistance());
            p.setString(2, o.getUsername());
            int affectedRows = p.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}