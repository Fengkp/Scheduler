package view;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Customer;
import utils.AppointmentDatabase;
import utils.CustomerDatabase;
import java.io.IOException;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NewAppointmentController extends UniversalController {
    @FXML
    private TextField appointmentTypeTxt;
    @FXML
    private Button confirmBtn, cancelBtn, deleteBtn;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private ComboBox<String> startHourCombo, startMinutesCombo, endHourCombo, endMinutesCombo;
    @FXML
    private ComboBox<Customer> customerCombo;

    private boolean isNewAppointment = true;
    private Appointment appointment;
    public ObservableList<String> hours = FXCollections.observableArrayList();
    public ObservableList<String> minutes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        deleteBtn.setVisible(false);
        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "15", "30", "45");
        startHourCombo.setItems(hours);
        startMinutesCombo.setItems(minutes);
        endHourCombo.setItems(hours);
        endMinutesCombo.setItems(minutes);
        customerCombo.setItems(CustomerDatabase.getInstance().getCustomers());
        //test();
    }

    public void confirmBtn(ActionEvent event) throws IOException, SQLException {
        try {
            LocalDate date = appointmentDatePicker.getValue();
            String startHour = startHourCombo.getValue();
            String startMinutes = startMinutesCombo.getValue();
            String endHour = endHourCombo.getValue();
            String endMinutes = endMinutesCombo.getValue();

            LocalDateTime startLDT = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                    date.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMinutes));
            LocalDateTime endLDT = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                    date.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMinutes));

            if (isValidAppointment(startLDT, endLDT)) {
                appointment = new Appointment(appointmentTypeTxt.getText(), startLDT, endLDT);
                appointment.setCustomerId(customerCombo.getValue().getId());
                appointment.setCustomerName(customerCombo.getValue().getName());

                if (isNewAppointment)
                    AppointmentDatabase.getInstance().addAppointment(appointment);
                else
                    AppointmentDatabase.getInstance().updateAppointment(appointment.getId(), appointment);

                AppointmentDatabase.getInstance().refreshAppointments();
                cancelBtn(event);
            }
        } catch (NullPointerException | NumberFormatException ex) {
            System.out.println("EMPTY FIELDS");
        }
    }


    public void cancelBtn(ActionEvent event) throws IOException {
        newWindow(event, "MainView.fxml");
    }

    public void deleteBtn(ActionEvent event) throws SQLException, IOException {
        AppointmentDatabase.getInstance().deleteAppointment(appointment.getId());
        AppointmentDatabase.getInstance().refreshAppointments();
        cancelBtn(event);
    }

    public void editAppointment(Appointment appointment) {
        deleteBtn.setVisible(true);
        isNewAppointment = false;
        this.appointment = appointment;

        appointmentDatePicker.setValue(appointment.getStartTime().toLocalDate());
        startHourCombo.setValue(Integer.toString(appointment.getStartTime().getHour()));
        startMinutesCombo.setValue(Integer.toString(appointment.getStartTime().getMinute()));
        endHourCombo.setValue(Integer.toString(appointment.getEndTime().getHour()));
        endMinutesCombo.setValue(Integer.toString(appointment.getEndTime().getMinute()));
        appointmentTypeTxt.setText(appointment.getAppointmentType());
        customerCombo.setValue(CustomerDatabase.getInstance().getCustomer(appointment.getCustomerId()));
    }

    private boolean isValidAppointment(LocalDateTime start, LocalDateTime end) throws SQLException {
        if (appointmentTypeTxt.getText().trim().isEmpty() || customerCombo.getValue() == null) {
            System.out.println("EMPTY FIELDS");
            return false;
        }
        if (!isValidDate(start, end)) {
            System.out.println("This appointment does not contain a valid date or time.");
            return false;
        }
        if (AppointmentDatabase.getInstance().isAppointmentTimeOverlapping(start, end)) {
            System.out.println("This appointment overlaps an already existing appointment.");
            return false;
        }
        return true;
    }

    private boolean isValidDate(LocalDateTime start, LocalDateTime end) {
        int openHour = 9;
        int closeHour = 17;
        LocalDateTime now = LocalDateTime.now();

        if (start.isBefore(now) || end.isBefore(now))
            return false;
        if (start.isAfter(end) || end.isBefore(start))
            return false;
        if (start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY
                || end.getDayOfWeek() == DayOfWeek.SATURDAY || end.getDayOfWeek() == DayOfWeek.SUNDAY)
            return false;
        if (start.getHour() < openHour || start.getHour() >= closeHour)
            return false;
        if (end.getHour() <= openHour || end.getHour() > closeHour)
            return false;
        return true;
    }

//    private void test() {
//        appointmentDatePicker.setValue(LocalDate.of(2019, 10, 1));
//        startHourCombo.setValue("15");
//        startMinutesCombo.setValue("15");
//        endHourCombo.setValue("16");
//        endMinutesCombo.setValue("15");
//        appointmentTypeTxt.setText("Scrum");
//    }
}
