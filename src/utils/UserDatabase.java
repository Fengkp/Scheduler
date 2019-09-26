package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase {
    // Authenticates user for login window
    public static boolean authenticateUser(String username, String password) throws SQLException {
        ResultSet results = GetData.getDBResults("SELECT * FROM user WHERE userName = '" + username + "' AND password = '" + password + "'");

        if (results.next())
            return true;
        return false;
    }
}
