package view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import model.Appointment;
import utils.AppointmentDatabase;
import utils.CustomerDatabase;
import utils.UserDatabase;

public class LoginController extends UniversalController implements Initializable {
    @FXML
    private TextField userNameText;
    @FXML
    private TextField passwordText;
    @FXML
    private Label usernameLbl;
    @FXML
    private Label passwordLbl;
    @FXML
    private Button loginBtn;
    private ResourceBundle languageRB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Locale.setDefault(new Locale("de", "de"));
        languageRB = ResourceBundle.getBundle("language_files/rb", Locale.getDefault());
        usernameLbl.setText(languageRB.getString("username"));
        passwordLbl.setText(languageRB.getString("password"));
        loginBtn.setText(languageRB.getString("login"));
        loginBtn.setOnAction((ActionEvent e) -> {                                                                           // Only one button on this screen. I felt it was
            try {                                                                                                           // most appropiate to use a lambda here, instead of
                if (UserDatabase.getInstance().authenticateUser(userNameText.getText(), passwordText.getText())) {          // a new method.
                    readyMainView();
                    newWindow(e, "MainView.fxml", "Appointments");
                }
                else
                    errorBox(languageRB.getString("loginErrorTitle"), languageRB.getString("loginError"));
            }
            catch (SQLException s) {
                s.printStackTrace();
            }
            catch (IOException i) {
                i.printStackTrace();
            }
        });
    }

    private void readyMainView() throws SQLException {
        CustomerDatabase.getInstance().setCustomers();
        AppointmentDatabase.getInstance().setAppointments();
        if (!AppointmentDatabase.getInstance().getAppointmentsStartingSoon().isEmpty())
            appointmentsStartingSoon();
        CustomerDatabase.getInstance().setCities();
    }

    private void appointmentsStartingSoon() {
        StringBuilder appointmentsStartingSoon = new StringBuilder();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointments starting soon:");
        alert.setTitle("APPOINTMENT ALERT");
        DateTimeFormatter startTime = DateTimeFormatter.ofPattern("HH:mm");

        for (Appointment appointment : AppointmentDatabase.getInstance().getAppointmentsStartingSoon())
            appointmentsStartingSoon.append(appointment.getAppointmentType() + " starting within 15 minutes at "
                    + startTime.format(appointment.getStartTime()) + ".\n");
        alert.setContentText(appointmentsStartingSoon.toString());
        alert.showAndWait();
    }
}
