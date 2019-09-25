package model;

public class User extends Person {

    private String password;

    public User(){}

    public User(String userName, String password) {
        super.setName(userName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
