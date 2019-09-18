package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static Connection connection = null;

    private final String DB_URL = "jdbc:mysql://52.206.157.109/U06mzl";
    private final String DBUSER = "U06mzl";
    private final String DBPASS = "53688811361";

    private DatabaseConnection() {}

    public static DatabaseConnection getInstance() {
        if (instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
            System.out.println("Connected to database");
        }
        catch (SQLException e) {
            System.out.println("Connection failed.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        try {
            connection.close();
            System.out.println("Disconnected from database.");
        }
        catch (SQLException e) {
            System.out.println("No connection to server.");
        }
    }
}
