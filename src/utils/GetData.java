package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.*;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class GetData {
//    private  static Connection dBConnection = DatabaseConnection.getInstance().getConnection();
    private static ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsThisMonth = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsThisWeek = FXCollections.observableArrayList();

    // Done, but not verified. Need to  see if it's actually converting properly according to system default zone
    // and whatever timezone the DB is in.
    // **** Also remember to repopulate DB with original data from script or start a new DB.
    // 15 minute alert
    public static ArrayList<String> getAppointmentsSoon() throws SQLException {
        ArrayList<String> appointmentsStartingSoon = new ArrayList<>();
        LocalDateTime nowLDT = LocalDateTime.now();

        ResultSet results = getDBResults("SELECT * FROM appointment");

        while (results.next()) {
            ZonedDateTime appointmentUTC = convertToLocal(results.getTimestamp("start").toLocalDateTime());
            Duration duration = Duration.between(nowLDT, appointmentUTC);
            if (duration.toMinutes() <= 15 && duration.toMinutes() >= 0)
                appointmentsStartingSoon.add(results.getString("type") + " starting soon with "
                + results.getString("customerId") + " at " + appointmentUTC.toString());
        }

        return appointmentsStartingSoon;
    }

    private static ZonedDateTime convertToLocal(LocalDateTime time) {
        ZonedDateTime timeZDT = time.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime timeUTC = timeZDT.withZoneSameInstant(ZoneId.of("UTC"));
        return timeUTC;
    }

    // Authenticates user for login window
    public static boolean authenticateUser(String username, String password) throws SQLException {
        getAppointmentsSoon();
        ResultSet results = getDBResults("SELECT * FROM user WHERE userName = '" + username + "' AND password = '" + password + "'");

        if (results.next())
            return true;
        return false;
    }

    // Return ObservableList to display on calendar table
    public static void setAppointments() throws SQLException {
        Month thisMonth = LocalDateTime.now().getMonth();

        if (appointmentsList.isEmpty()) {
            ResultSet results = getDBResults("SELECT * FROM appointment WHERE start >= '" + LocalDateTime.now() + "'");

            while (results != null && results.next()) {
                Appointment newAppointment = new Appointment(results.getString("appointmentId"), results.getString("customerId"),
                        results.getString("type"), results.getTimestamp("start").toLocalDateTime(), results.getTimestamp("end").toLocalDateTime());
                appointmentsList.add(newAppointment);

                if (newAppointment.getStartTime().getMonth() == thisMonth)
                    appointmentsThisMonth.add(newAppointment);
            }
            setAppointmentsThisWeek();
        }
    }

    public static ObservableList<Appointment> getAppointments() {
        return appointmentsList;
    }

    public static ObservableList<Appointment> getAppointmentsThisWeek() {
        return appointmentsThisWeek;
    }

    public static ObservableList<Appointment> getAppointmentsThisMonth() {
        return appointmentsThisMonth;
    }

    private static void setAppointmentsThisWeek() throws SQLException{
        LocalDateTime start = LocalDateTime.now(ZoneId.of(ZoneId.systemDefault().toString())).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDateTime end = start.plusDays(5);

        ResultSet results = getDBResults("SELECT * FROM appointment WHERE start >= '" + start + "' AND start <= '" + end + "'");

        while (results != null && results.next()) {
            Appointment newAppointment = new Appointment(results.getString("appointmentId"), results.getString("customerId"),
                    results.getString("type"), results.getTimestamp("start").toLocalDateTime(), results.getTimestamp("end").toLocalDateTime());
            appointmentsThisWeek.add(newAppointment);
        }
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

    public static String getCustomerName(String id) throws SQLException {
        ResultSet results = getDBResults("SELECT * FROM customer WHERE customerId = '" + id + "'");
        results.next();
        return results.getString("customerName");
    }
}
