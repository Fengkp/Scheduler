package utils;
import java.sql.*;
import java.time.*;

public class GetData {
    public static LocalDateTime convertToLocal(LocalDateTime time) {
        ZonedDateTime timeZDT = time.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime timeUTC = timeZDT.withZoneSameInstant(ZoneId.of("UTC"));
        return timeUTC.toLocalDateTime();
    }

    public static LocalDateTime convertToUTC(LocalDateTime time) {
        ZonedDateTime timeUTC = time.atZone(ZoneId.of("UTC"));
        return timeUTC.toLocalDateTime();
    }


    public static ResultSet getDBResults(String query) {
        try {
            Statement statement = DatabaseConnection.getInstance().getConnection().createStatement();
            ResultSet results = statement.executeQuery(query);
            return results;
        }
        catch (SQLException ex) {
            System.out.println("No results.");
        }
        return null;
    }
}
