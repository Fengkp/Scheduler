package utils;

import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;

public class GetData {
    private Connection dBConnection = DatabaseConnection.getInstance().getConnection();
    private ObservableList<Appointment> appointmentsList;
    private ResultSet results;

    // Done, but not verified. Need to  see if it's actually converting properly according to system default zone
    // and whatever timezone the DB is in.
    // **** Also remember to repopulate DB with original data from script or start a new DB.

    // 15 minute alert
    public ArrayList<String> getAppointmentsSoon() {
        ArrayList<String> appointmentsStartingSoon = new ArrayList<>();
        LocalDateTime nowLDT = LocalDateTime.now();

        try {
            results = getDBResults("SELECT * FROM appointment");

            while (results.next()) {
                ZonedDateTime appointmentUTC = convertToLocal(results.getTimestamp("start").toLocalDateTime());
                Duration duration = Duration.between(nowLDT, appointmentUTC);
                if (duration.toMinutes() <= 15 && duration.toMinutes() >= 0)
                    appointmentsStartingSoon.add(results.getString("type") + " starting soon with "
                    + results.getString("customerId") + " at " + appointmentUTC.toString());
            }
        }
        catch (SQLException ex) {
            // Work on exceptions
            System.out.println("Alert window error.");
        }
        return appointmentsStartingSoon;
    }

    public ZonedDateTime convertToLocal(LocalDateTime time) {
        ZonedDateTime timeZDT = time.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime timeUTC = timeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        return timeUTC;
    }

    // Authenticates user for login window
    // Make static
    public boolean authenticateUser(String username, String password) {
        getAppointmentsSoon();
        try {
            results = getDBResults("SELECT username, password FROM user");

            while (results.next())
                if (results.getString("username").equals(username)
                        && results.getString("password").equals(password))
                    return true;

        } catch (SQLException ex) {
            System.out.println("No connection to server.");
        }
        return false;
    }

    // Return ObservableList to display on calendar table
    public ObservableList<Appointment> getAppointments() {
        if (appointmentsList.isEmpty()) {
            try {
                results = getDBResults("SELECT * FROM appointments");

                while (results.next()) {
                    Appointment newAppointment = new Appointment(results.getString("appointmentId"), results.getString("customerId"),
                            results.getString("appointmentType"), results.getTimestamp("start").toLocalDateTime(), results.getTimestamp("end").toLocalDateTime());
                    appointmentsList.add(newAppointment);
                }
                return appointmentsList;
            }
            catch (SQLException ex) {
                System.out.println("Filling main table error.");
            }
        }
        return appointmentsList;
    }

    public ResultSet getDBResults(String query) {
        try {
            Statement statement = dBConnection.createStatement();
            ResultSet results = statement.executeQuery(query);
            return results;
        }
        catch (SQLException ex) {
            System.out.println("No results.");
        }
        return null;
    }
}
