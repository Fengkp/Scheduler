package utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

public class AppointmentDatabase {
    private static AppointmentDatabase instance;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentsThisMonth = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentsThisWeek = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentsStartingSoon = FXCollections.observableArrayList();

    private AppointmentDatabase() {}

    public static AppointmentDatabase getInstance() {
        if (instance == null)
            instance = new AppointmentDatabase();
        return instance;
    }

    public void addAppointment(Appointment appointment) throws SQLException {
        String nA = "N/A";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        Statement statement = DatabaseConnection.getInstance().getConnection().createStatement();
        statement.executeUpdate("INSERT INTO appointment (userId, customerId, title, description, location, contact, url, createDate, " +
                "createdBy, lastUpdateBy, type, start, end) VALUES ('"
                + UserDatabase.getInstance().getUser().getId() + "', '" + appointment.getCustomerId()
                + "', '" + nA + "', '" + nA + "', '" + nA + "', '" + nA  + "', '" + nA + "', '" + now + "', '"
                + UserDatabase.getInstance().getUser().getName() + "', '" + UserDatabase.getInstance().getUser().getName()
                + "', '" + appointment.getAppointmentType() + "', '" + appointment.getStartTime() + "', '" + appointment.getEndTime() + "')");
    }

    private Appointment createAppointment(ResultSet results) throws SQLException{
        Appointment appointment = new Appointment(results.getString("type"),
                GetData.getInstance().convertToLocal(results.getTimestamp("start").toLocalDateTime()),
                GetData.getInstance().convertToLocal(results.getTimestamp("end").toLocalDateTime()));
        appointment.setId(results.getInt("appointmentId"));
        appointment.setCustomerId(results.getInt("customerId"));
        appointment.setCustomerName(CustomerDatabase.getInstance().getCustomer(results.getInt("customerId")).getName());

        return appointment;
    }

    public ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments() throws SQLException {
        Month thisMonth = LocalDateTime.now().getMonth();

        if (appointments.isEmpty()) {
            ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM appointment WHERE start >= '" + LocalDateTime.now() + "'");

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

    public ObservableList<Appointment> getAppointmentsStartingSoon() {
        return appointmentsStartingSoon;
    }

    public void setAppointmentsStartingSoon() throws SQLException {
        int timeUntil = 15;
        LocalDateTime now = GetData.getInstance().convertToUTC(LocalDateTime.now());
        LocalDateTime timeFromNow = now.plusMinutes(timeUntil);
        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM appointment WHERE start >= '" + now +
                "' AND start <= '" + timeFromNow + "'");

        while (results.next()) {
            Appointment appointment = new Appointment(results.getString("type"),
                    GetData.getInstance().convertToLocal(results.getTimestamp("start").toLocalDateTime()),
                    GetData.getInstance().convertToLocal(results.getTimestamp("end").toLocalDateTime()));
            appointmentsStartingSoon.add(appointment);
        }
    }

    public ObservableList<Appointment> getAppointmentsThisWeek() {
        return appointmentsThisWeek;
    }

    private void setAppointmentsThisWeek() throws SQLException{
//        LocalDateTime start = LocalDateTime.now(ZoneId.of(ZoneId.systemDefault().toString())).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDateTime start = LocalDateTime.now(ZoneId.of(ZoneId.systemDefault().toString())).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
//        start = start.plusWeeks(1);
        LocalDateTime end = start.plusDays(5);

        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM appointment WHERE start >= '" + start + "' AND start <= '" + end + "'");

        while (results.next())
            appointmentsThisWeek.add(createAppointment(results));
    }

    public ObservableList<Appointment> getAppointmentsThisMonth() {
        return appointmentsThisMonth;
    }

    public boolean isAppointmentTimeOverlapping(LocalDateTime start, LocalDateTime end) throws SQLException {
        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM appointment WHERE start <= '" + end + "' AND end >='" + end + "'");
        if (!results.next() == false)
            return true;
        results = GetData.getInstance().getDBResults("SELECT * FROM appointment WHERE start <= '" + start + "' AND end >='" + start + "'");
        if (!results.next() == false)
            return true;
        return false;
    }

    public void refreshAppointments() throws SQLException {
        appointments.clear();
        appointmentsThisMonth.clear();
        appointmentsThisWeek.clear();
        setAppointments();
    }

    public void updateAppointment(int appointmentToEdit, Appointment appointment) throws SQLException {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        GetData.getInstance().updateDB("UPDATE appointment SET customerId = '" + appointment.getCustomerId() + "', type = '" + appointment.getAppointmentType() + "', start = '"
                + appointment.getStartTime() + "', end = '" + appointment.getEndTime() + "', lastUpdate = '" + now + "', lastUpdateBy = '"
                + UserDatabase.getInstance().getUser() + "' WHERE appointmentId = '" + appointmentToEdit + "'");
    }

    public void deleteAppointment(int appointmentToDelete) throws SQLException {
        GetData.getInstance().updateDB("DELETE FROM appointment WHERE appointmentId = '" + appointmentToDelete + "'");
    }
}
