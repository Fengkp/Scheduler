package login;

import java.sql.*;

public class User {
    private static Connection conn = null;

    public boolean authenticate(String username, String password) {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://52.206.157.109/U06mzl";

        //  Database credentials
        final String DBUSER = "U06mzl";
        final String DBPASS = "53688811361";

        try {
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user");
            ResultSet results = stmt.executeQuery();

            while (results.next())
                System.out.println(results.getString(2));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        return false;
    }
}
