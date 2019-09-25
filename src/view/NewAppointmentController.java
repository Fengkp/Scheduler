package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Address;
import model.Appointment;
import model.Customer;
import utils.DatabaseConnection;
import utils.GetData;

import java.io.IOException;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NewAppointmentController {
    @FXML
    private TextField appointmentTypeTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField addressTxt;
    @FXML
    private TextField cityTxt;
    @FXML
    private TextField countryTxt;
    @FXML
    private TextField postalCodeTxt;
    @FXML
    private TextField phoneNumberTxt;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button autoFillBtn;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private ComboBox<String> startHourCombo;
    @FXML
    private ComboBox<String> startMinutesCombo;
    @FXML
    private ComboBox<String> endHourCombo;
    @FXML
    private ComboBox<String> endMinutesCombo;

    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "15", "30", "45");
        startHourCombo.setItems(hours);
        startMinutesCombo.setItems(minutes);
        endHourCombo.setItems(hours);
        endMinutesCombo.setItems(minutes);
    }

    public void confirmBtn() throws SQLException {
        LocalDate date = appointmentDatePicker.getValue();
        String startHour = startHourCombo.getValue();
        String startMinutes = startMinutesCombo.getValue();
        String endHour = endHourCombo.getValue();
        String endMinutes = endMinutesCombo.getValue();

        LocalDateTime startLDT = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                date.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMinutes));
        LocalDateTime endLDT = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                date.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMinutes));

        if (isValidAppointment(startLDT, endLDT));
            // Add to DB, refresh arrays, display table again, close window
//        newAppointment.setStartTime(startTimeTxt);
    }

    public void cancelBtn() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public boolean isValidAppointment(LocalDateTime start, LocalDateTime end) throws SQLException {
        // Time
        if (!isValidDate(start, end)) {
            System.out.println("This appointment does not contain a valid date or time.");
            return false;
        }
        if (isAppointmentTimeOverlapping(start, end)) {
            System.out.println("This appointment overlaps an already existing appointment.");
            return false;
        }
        // Customer details
        // CHECK FOR INVALID FIELDS BEFORE MAKING NEW CUSTOMER
        test();
        Customer newCustomer = new Customer (customerExists(nameTxt.getText()), nameTxt.getText(), addressTxt.getText(),
                cityTxt.getText(), countryTxt.getText(), postalCodeTxt.getText(), phoneNumberTxt.getText());
        if (newCustomer.getId() != -1)
            System.out.println(compareAddress(newCustomer));

        return true;
    }

    public boolean compareAddress(Customer customer) throws SQLException {
        ResultSet results = GetData.getDBResults("SELECT address, city, country, postalCode, phone " +
                "FROM address, city, country, address, address " +
                "WHERE address = '" + customer.getAddress() + "' AND city = '" + customer.getCity() +
                "' AND country = '" + customer.getCountry() + "' AND postalCode = '" + customer.getPostalCode() +
                "' AND phone = '" + customer.getPhone() + "'");
        if (results.next() == false)
            return false;
        return true;
    }

    public void test() {
        nameTxt.setText("John Doe");
        addressTxt.setText("123 Main");
        cityTxt.setText("New York");
        countryTxt.setText("US");
        postalCodeTxt.setText("11111");
        phoneNumberTxt.setText("555-1212");
    }

    public int customerExists(String customerName) throws SQLException {
        ResultSet results = GetData.getDBResults("SELECT * FROM customer WHERE customerName ='" + customerName + "'");

        if (results.next() == false)
            return -1;
        return results.getInt("customerId");
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

    private boolean isAppointmentTimeOverlapping(LocalDateTime start, LocalDateTime end) throws SQLException {
        ResultSet results = GetData.getDBResults("SELECT * FROM appointment WHERE start <= '" + end + "' AND end >='" + end + "'");
        if (!results.next() == false)
            return true;
        results = GetData.getDBResults("SELECT * FROM appointment WHERE start <= '" + start + "' AND end >='" + start + "'");
        if (!results.next() == false)
            return true;
        return false;
    }

}
