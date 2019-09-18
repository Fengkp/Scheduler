package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Appointment;
import utils.GetData;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, String> customerNameColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endTimeColumn;

    // Fill table
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ** Doesn't work
        GetData getAlerts = new GetData();
        for (String appointment : getAlerts.getAppointmentsSoon()) {
            if (getAlerts.getAppointmentsSoon().isEmpty()) {
                System.out.println("No alerts.");
                break;
            }
            System.out.println(appointment);
        }
        // **


    }


}
