package utils;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase {
    private static User user = null;
    private static UserDatabase instance;

    private UserDatabase() {}

    public static UserDatabase getInstance() {
        if (instance == null)
            instance = new UserDatabase();
        return instance;
    }

    public User getUser() {
        return user;
    }

    // Authenticates user for login window
    public boolean authenticateUser(String username, String password) throws SQLException {
        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM user WHERE userName = '" + username + "' AND password = '" + password + "'");

        if (results.next()) {
            user = new User(results.getString("userName"));
            user.setId(results.getInt("userId"));
            return true;
        }
        return false;
    }
}
