package view;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Appointment;
import utils.AppointmentDatabase;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MainController extends UniversalController{
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn, customerNameColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn, endTimeColumn;
    @FXML
    private Button allBtn, weekBtn, monthBtn, newAppointmentBtn, customerRecordsBtn, reportsBtn;

    @FXML
    public void initialize() {
        setTable(AppointmentDatabase.getInstance().getAppointments());
        appointmentTable.getSelectionModel().selectedItemProperty().addListener(        // This is the only time within this file that I'll use a listener.
                (observable, oldValue, newValue) -> {                                   // No need to create a separate method for it.
                    try {
                        if (newValue != null) {
                            try {
                                updateAppointment(newValue);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Nothing selected.");
                    }
                });
    }

    public void setTable(ObservableList<Appointment> appointments) {
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
        newWindow(event, "AppointmentView.fxml", "Create Appointment");
    }

    public void customerRecordsBtn(ActionEvent event) throws IOException {
        newWindow(event, "CustomerRecordsView.fxml", "Create or Edit Customers");
    }
    public void reportsBtn(ActionEvent event) throws IOException {
        newWindow(event, "ReportsView.fxml", "Reports");
    }

    public void updateAppointment(Appointment appointment) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentView.fxml"));
        Parent root = loader.load();

        AppointmentController controller = loader.getController();
        controller.editAppointment(appointment);

        Scene scene = new Scene(root);
        Stage window = (Stage) newAppointmentBtn.getScene().getWindow();
        window.setResizable(false);
        window.setTitle("Edit Appointment");
        window.setScene(scene);
        window.show();
    }
}
