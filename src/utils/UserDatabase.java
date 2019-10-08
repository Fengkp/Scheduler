package utils;

import model.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public boolean authenticateUser(String username, String password) throws SQLException, IOException {
        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM user WHERE userName = '" + username + "' AND password = '" + password + "'");

        if (results.next()) {
            user = new User(results.getString("userName"));
            user.setId(results.getInt("userId"));
            userActivity();
            return true;
        }
        return false;
    }

    private void userActivity() throws IOException {
        List<String> login = new ArrayList();
        login.add(user.toString() + " logged in on " + LocalDateTime.now().toString());
        GetData.getInstance().outputToTxt("logs/useractivity.txt", login);
    }
}
