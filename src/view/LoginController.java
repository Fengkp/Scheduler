package view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Node;
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
    private String userName;
    private String password;
    private ResourceBundle languageRB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Locale.setDefault(new Locale("de", "de"));
        languageRB = ResourceBundle.getBundle("language_files/rb", Locale.getDefault());
        usernameLbl.setText(languageRB.getString("username"));
        passwordLbl.setText(languageRB.getString("password"));
        loginBtn.setText(languageRB.getString("login"));
    }

    public void loginBtn(ActionEvent event) throws IOException, SQLException {
        userName = userNameText.getText();
        password = passwordText.getText();

        if (UserDatabase.getInstance().authenticateUser(userName, password)) {
            System.out.println(UserDatabase.getInstance().getUser() + " authenticated.");
            readyMainView();

            newWindow(event, "MainView.fxml");
        }
        else {
            System.out.println("Incorrect login information");
            errorBox(languageRB.getString("loginErrorTitle"), languageRB.getString("loginError"));
        }
    }

    public void errorBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void readyMainView() throws SQLException {
        for (Appointment appointment : AppointmentDatabase.getInstance().getAppointmentsStartingSoon()) {
            if (AppointmentDatabase.getInstance().getAppointmentsStartingSoon().isEmpty()) {
                System.out.println("No alerts.");
                break;
            }
            System.out.println(appointment);
        }
        CustomerDatabase.getInstance().setCustomers();
        AppointmentDatabase.getInstance().setAppointments();
        System.out.println(AppointmentDatabase.getInstance().getAppointmentsStartingSoon());
        CustomerDatabase.getInstance().setCities();
    }
}
