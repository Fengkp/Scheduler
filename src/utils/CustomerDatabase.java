package utils;
import model.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDatabase {
    public static Customer getCustomer(int id) throws SQLException {
        ResultSet results = GetData.getDBResults("SELECT customer.customerId, customer.customerName, " +
                "address.address, city.city, country.country, address.postalCode, address.phone " +
                "FROM customer INNER JOIN address ON customer.addressId = address.addressId INNER JOIN city " +
                "ON address.cityId = city.cityId INNER JOIN country ON city.countryId = country.countryId WHERE customerId = '" + id + "'");

        if (results.next() == true) {
            Customer customer = new Customer(results.getString("customerName"), results.getString("address"),
                    results.getString("city"), results.getString("country"),
                    results.getString("postalCode"), results.getString("phone"));
            customer.setId(results.getInt("customerId"));
            return customer;
        }
        return null;
    }

    public static int customerExists(String name) throws SQLException {
        ResultSet results = GetData.getDBResults("SELECT * FROM customer WHERE customerName = '" + name + "'");

        if (results.next() == true)
            return results.getInt("customerId");
        return -1;
    }
}
