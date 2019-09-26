package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appointment;
import java.io.IOException;
import java.time.LocalDateTime;
import static utils.AppointmentDatabase.*;

public class MainController {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn, customerNameColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn, endTimeColumn;
    @FXML
    private Button allBtn, weekBtn, monthBtn, newAppointmentBtn;

    // Fill table
    @FXML
    public void initialize() {
        for (Appointment appointment : getAppointmentsStartingSoon()) {
            if (getAppointmentsStartingSoon().isEmpty()) {
                System.out.println("No alerts.");
                break;
            }
            System.out.println(appointment);
        }
        setTable(getAppointments());
    }

    private void setTable(ObservableList<Appointment> appointments) {
        appointmentTypeColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentTypeProperty());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        appointmentTable.setItems(appointments);
    }

    public void allBtn() {
        setTable(getAppointments());
    }

    public void weekBtn() {
        setTable(getAppointmentsThisWeek());
    }

    public void monthBtn() {
        setTable(getAppointmentsThisMonth());
    }

    public void newAppointmentBtn(ActionEvent event) throws IOException {
        AnchorPane newAppointmentPane = FXMLLoader.load(getClass().getResource("NewAppointmentView.fxml"));
        Stage newAppointmentStage = new Stage();
        newAppointmentStage.initModality(Modality.WINDOW_MODAL);
        newAppointmentStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Scene newAppointmentScene = new Scene(newAppointmentPane);
        newAppointmentStage.setScene(newAppointmentScene);
        newAppointmentStage.showAndWait();
    }

}
