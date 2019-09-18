package model;

public class Address {
    private int id;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String phone;

    public Address(String address, String city, String country, String postalCode, String phone) {
        setId();
        this.address = address;
        this.city = city;           // Cities may already exist w/in db
        this.country = country;     // Either find it and set it, or create
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public void setId() {
        // Generate id based on last address entry and adding 1
    }

}
