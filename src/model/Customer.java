package model;

public class Customer extends Person {
    private String address;
    private int addressId;
    private City city;
    private String phone;

    public Customer(String name, String address, City city, String phone) {
        super.setName(name);
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int id) {
        addressId = id;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
