package model;

public class User extends Person {
    private String password;

    public User(String userName) {
        super.setName(userName);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
