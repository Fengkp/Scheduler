package utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.*;

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
}
