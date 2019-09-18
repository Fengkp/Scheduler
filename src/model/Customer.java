package model;

public class Customer extends Person {
    private String addressId;

    public Customer(String id, String name, String addressId) {
        super.setId(id);
        super.name = name;
        this.addressId = addressId;
    }



}
