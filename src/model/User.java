package model;

import utils.DatabaseConnection;

import java.sql.*;

public class User extends Person {

    private String password;

    public User(){}

    public User(String userName, String password) {
        super.setId("user");
        super.name = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
