package login.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import login.User;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userNameText;
    @FXML
    private TextField passwordText;

    private String userName;
    private String password;

    public void loginBtn() throws IOException {
        userName = userNameText.getText();
        password = passwordText.getText();

        User authenticator = new User();
        if (authenticator.authenticate(userName, password)) {
            System.out.println("User Authenticated.");
        }

//        System.out.println(userName);
//        System.out.println(password);
    }
}
