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
import utils.AppointmentDatabase;

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
    private Button allBtn, weekBtn, monthBtn, newAppointmentBtn, newCustomerBtn;

    @FXML
    public void initialize() {
        for (Appointment appointment : AppointmentDatabase.getInstance().getAppointmentsStartingSoon()) {
            if (AppointmentDatabase.getInstance().getAppointmentsStartingSoon().isEmpty()) {
                System.out.println("No alerts.");
                break;
            }
            System.out.println(appointment);
        }
        setTable(AppointmentDatabase.getInstance().getAppointments());
    }

    private void setTable(ObservableList<Appointment> appointments) {
        appointmentTypeColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentTypeProperty());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        appointmentTable.setItems(appointments);
    }

    public void allBtn() {
        setTable(AppointmentDatabase.getInstance().getAppointments());
    }

    public void weekBtn() {
        setTable(AppointmentDatabase.getInstance().getAppointmentsThisWeek());
    }

    public void monthBtn() {
        setTable(AppointmentDatabase.getInstance().getAppointmentsThisMonth());
    }

    public void newAppointmentBtn(ActionEvent event) throws IOException {
        newWindow(event, "NewAppointmentView.fxml");
    }

    public void newCustomerBtn(ActionEvent event) throws IOException {
        newWindow(event, "NewCustomerView.fxml");
    }

    private void newWindow(ActionEvent event, String viewFXML) throws IOException {
        AnchorPane newAppointmentPane = FXMLLoader.load(getClass().getResource(viewFXML));
        Stage newAppointmentStage = new Stage();
        newAppointmentStage.initModality(Modality.WINDOW_MODAL);
        newAppointmentStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Scene newAppointmentScene = new Scene(newAppointmentPane);
        newAppointmentStage.setScene(newAppointmentScene);
        newAppointmentStage.showAndWait();
    }

}
