package utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import static utils.CustomerDatabase.getCustomer;
import static utils.GetData.*;

public class AppointmentDatabase {
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsThisMonth = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsThisWeek = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsStartingSoon = FXCollections.observableArrayList();

    public static Appointment createAppointment(ResultSet results) throws SQLException{
        Appointment appointment = new Appointment(results.getString("type"),
                results.getTimestamp("start").toLocalDateTime(), results.getTimestamp("end").toLocalDateTime());
        appointment.setId(results.getInt("appointmentId"));
        appointment.setId(results.getInt("customerId"));
        appointment.setCustomerName(getCustomer(results.getInt("customerId")).getName());

        return appointment;
    }

    public static ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    // Return ObservableList to display on calendar table
    public static void setAppointments() throws SQLException {
        Month thisMonth = LocalDateTime.now().getMonth();

        if (appointments.isEmpty()) {
            ResultSet results = getDBResults("SELECT * FROM appointment WHERE start >= '" + LocalDateTime.now() + "'");

            while (results != null && results.next()) {
                Appointment newAppointment = createAppointment(results);
                appointments.add(newAppointment);

                if (newAppointment.getStartTime().getMonth() == thisMonth)
                    appointmentsThisMonth.add(newAppointment);
            }
            setAppointmentsThisWeek();
            setAppointmentsStartingSoon();
        }
    }

    public static ObservableList<Appointment> getAppointmentsStartingSoon() {
        return appointmentsStartingSoon;
    }

    public static void setAppointmentsStartingSoon() throws SQLException {
        int timeUntil = 15;
        LocalDateTime now = convertToUTC(LocalDateTime.now());
        LocalDateTime timeFromNow = now.plusMinutes(timeUntil);
        ResultSet results = getDBResults("SELECT * FROM appointment WHERE start >= '" + now +
                "' AND start <= '" + timeFromNow + "'");

        while (results.next()) {
            Appointment appointment = new Appointment(results.getString("type"),
                    convertToLocal(results.getTimestamp("start").toLocalDateTime()),
                    convertToLocal(results.getTimestamp("end").toLocalDateTime()));
            appointmentsStartingSoon.add(appointment);
        }
    }

    public static ObservableList<Appointment> getAppointmentsThisWeek() {
        return appointmentsThisWeek;
    }

    private static void setAppointmentsThisWeek() throws SQLException{
        LocalDateTime start = LocalDateTime.now(ZoneId.of(ZoneId.systemDefault().toString())).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDateTime end = start.plusDays(5);

        ResultSet results = getDBResults("SELECT * FROM appointment WHERE start >= '" + start + "' AND start <= '" + end + "'");

        while (results != null && results.next())
            appointmentsThisWeek.add(createAppointment(results));
    }

    public static ObservableList<Appointment> getAppointmentsThisMonth() {
        return appointmentsThisMonth;
    }

    public static boolean isAppointmentTimeOverlapping(LocalDateTime start, LocalDateTime end) throws SQLException {
        ResultSet results = GetData.getDBResults("SELECT * FROM appointment WHERE start <= '" + end + "' AND end >='" + end + "'");
        if (!results.next() == false)
            return true;
        results = GetData.getDBResults("SELECT * FROM appointment WHERE start <= '" + start + "' AND end >='" + start + "'");
        if (!results.next() == false)
            return true;
        return false;
    }
}
