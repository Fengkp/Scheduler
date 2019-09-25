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
import model.Appointment;

import java.io.IOException;
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

    public void confirmBtn(ActionEvent event) {
        LocalDate date = appointmentDatePicker.getValue();
        String startHour = startHourCombo.getValue();
        String startMinutes = startMinutesCombo.getValue();
        String endHour = endHourCombo.getValue();
        String endMinutes = endMinutesCombo.getValue();

        LocalDateTime startLDT = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                date.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMinutes));
        LocalDateTime endLDT = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                date.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMinutes));

        // Check if valid times (business hours, M-F, overlapping)

        Appointment newAppointment = new Appointment();
//        newAppointment.setStartTime(startTimeTxt);
    }

    public void cancelBtn(ActionEvent event) throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }

}
