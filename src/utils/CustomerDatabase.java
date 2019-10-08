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

    public void addCustomer(Customer customer, int customerToEdit) throws SQLException {
        int addressId = addressExists(customer);
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        if (addressId == -1) {
            addAddress(customer);
            addressId = addressExists(customer);
        }

        if (customerToEdit != -1)
            GetData.getInstance().updateDB("UPDATE customer SET customerName = '" + customer.getName() + "', addressId = '" + addressId + "', active = '"
                    + 1 + "', lastUpdate = '" + now + "', lastUpdateBy = '" + UserDatabase.getInstance().getUser() + "' WHERE customerId = '" + customerToEdit + "'");
        else
            GetData.getInstance().updateDB("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('"
                    + customer.getName() + "', '" + addressId + "', '" +  1 + "', '" + now + "', '" + UserDatabase.getInstance().getUser() + "', '"
                    + now + "', '" + UserDatabase.getInstance().getUser() + "')");
    }

    public void addAddress(Customer customer) throws SQLException {
        String nA = "N/A";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        GetData.getInstance().updateDB("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('"
                + customer.getAddress() + "', '" + nA + "', '" + customer.getCity().getCityId() + "', '" + customer.getCity().getPostalCode() + "', '" + customer.getPhone()
                + "', '" + now + "', '" + UserDatabase.getInstance().getUser() + "', '" + now + "', '" + UserDatabase.getInstance().getUser() + "')");
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
                + customer.getName() + "'");
        while (results != null && results.next())
            if (customers.get(results.getInt("customerId")).getPhone().equals(customer.getPhone()))
                return true;
        return false;
    }

    public int addressExists(Customer customer) throws SQLException {
        ResultSet results = GetData.getInstance().getDBResults("SELECT * FROM address WHERE address = '" + customer.getAddress() + "' AND cityId = '" + customer.getCity().getCityId() + "'");
        if (results != null && results.next())
            return results.getInt("addressId");
        return -1;
    }

    public void refreshCustomers() throws SQLException {
        customers.clear();
        setCustomers();
    }

    public void deleteCustomer(int customerToEdit) throws SQLException {
        GetData.getInstance().updateDB("DELETE FROM customer WHERE customerId = '" + customerToEdit + "'");
    }
}
