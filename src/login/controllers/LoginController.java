package login.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import login.User;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("language_files/rb", Locale.getDefault());
        usernameLbl.setText(rb.getString("username"));
        passwordLbl.setText(rb.getString("password"));
        loginBtn.setText(rb.getString("login"));
    }

    public void loginBtn() throws IOException {
        userName = userNameText.getText();
        password = passwordText.getText();

        User authenticator = new User();
        if (authenticator.authenticate(userName, password)) {
            System.out.println("User Authenticated.");

            Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else
            handleLoginError();
    }

    public void handleLoginError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

    }
}
