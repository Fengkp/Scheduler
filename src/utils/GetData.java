package utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.util.List;

public class GetData {
    private static GetData instance;

    private GetData() {}

    public static GetData getInstance() {
        if (instance == null)
            instance = new GetData();
        return instance;
    }

    public LocalDateTime convertToLocal(LocalDateTime time) {
        ZonedDateTime timeZDT = time.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime timeUTC = timeZDT.withZoneSameInstant(ZoneId.of("UTC"));
        return timeUTC.toLocalDateTime();
    }

    public LocalDateTime convertToUTC(LocalDateTime time) {
        ZonedDateTime timeUTC = time.atZone(ZoneId.of("UTC"));
        return timeUTC.toLocalDateTime();
    }

    public ResultSet getDBResults(String query) {
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

    public void updateDB(String query) throws SQLException {
        Statement statement = DatabaseConnection.getInstance().getConnection().createStatement();
        statement.executeUpdate(query);
    }

    public void outputToTxt(String destination, List<String> data) throws IOException {
        File file = new File(destination);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true))) {
            for (String string : data) {
                writer.write(string);
                writer.newLine();
            }
        }

    }

    public void deleteFile(String source) {
        File file = new File(source);
        if (file.exists())
            file.delete();
    }
}
