package utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.City;
import model.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CustomerDatabase {
    private static CustomerDatabase instance;
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<Integer, City> cities = new HashMap<>();

    private CustomerDatabase() {}

    public static CustomerDatabase getInstance() {
        if (instance == null)
            instance = new CustomerDatabase();
        return instance;
    }

    public void addCustomer(Customer customer) throws SQLException {
        String nA = "N/A";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        Statement statement = DatabaseConnection.getInstance().getConnection().createStatement();
        statement.executeUpdate("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('"
                + customer.getAddress() + "', " + nA + "', " + customer.getCity().getCityId() + "', " + customer.getCity().getPostalCode() + "', " + customer.getPhone()
                + "', " + now + "', " + UserDatabase.getInstance().getUser() + "', " + now + "', " + UserDatabase.getInstance().getUser() + "'");
        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM address WHERE address = '" + customer.getAddress() + "' AND cityId = '" + customer.getCity().getCityId());
        if (results.next())
            customer.setAddressId(results.getInt("addressId"));
        statement.executeUpdate("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdatedBy) VALUES ('"
                + customer.getName() + "', " + customer.getAddressId() + "', " +  nA + "', " + now + "', " + UserDatabase.getInstance().getUser() + "', "
                + now + "', " + UserDatabase.getInstance().getUser() + "'");
    }

    public void setCustomers() throws SQLException {
        ResultSet results = GetData.getInstance().getDBResults("SELECT customer.customerId, customer.addressId, customer.customerName, " +
                "address.address, city.city, country.country, address.postalCode, address.phone " +
                "FROM customer INNER JOIN address ON customer.addressId = address.addressId INNER JOIN city " +
                "ON address.cityId = city.cityId INNER JOIN country ON city.countryId = country.countryId");

        while (results.next()) {
            City city = new City(results.getString("city"));
            Customer customer = new Customer(results.getString("customerName"), results.getString("address"),
                    city, results.getString("phone"));
            customer.setId(results.getInt("customerId"));
            customer.setAddressId(results.getInt("addressId"));

            customers.put(customer.getId(), customer);
        }
    }

    public ObservableList<Customer> getCustomers() {
        return FXCollections.observableArrayList(new ArrayList<>(customers.values()));
    }

    public Customer getCustomer(int id) {
        return customers.get(id);
    }

    public void setCities() {
        cities.put(1, new City("New York"));
        cities.put(2, new City("Phoenix"));
        cities.put(3, new City("London"));
    }

    public ObservableList<City> getCities() {
        return FXCollections.observableArrayList(new ArrayList<>(cities.values()));
    }

    public boolean customerExists(Customer customer) throws SQLException {
        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM customer WHERE customerName = '"
                + customer.getName() + "' AND phone = '" + customer.getPhone());
        if (results.next() == false)
            return true;
        return false;
    }
}
