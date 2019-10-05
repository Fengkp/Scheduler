package view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.City;
import model.Customer;
import utils.CustomerDatabase;
import java.io.IOException;
import java.sql.SQLException;

public class NewCustomerController extends UniversalController {
    @FXML
    private TextField nameTxt, addressTxt, phoneTxt;
    @FXML
    private Button confirmBtn, cancelBtn;
    @FXML
    private ComboBox<City> cityCombo;

    @FXML
    public void initialize() {
        cityCombo.setItems(CustomerDatabase.getInstance().getCities());
    }

    public void confirmBtn(ActionEvent event) throws IOException, SQLException {
        Customer customer = new Customer(nameTxt.getText(), addressTxt.getText(), cityCombo.getValue(), phoneTxt.getText());
        if (isValidCustomer(customer)) {
            CustomerDatabase.getInstance().addCustomer(customer);
            CustomerDatabase.getInstance().refreshCustomers();
            System.out.println("Customer added.");
            cancelBtn(event);
        }
    }

    public void cancelBtn(ActionEvent event) throws IOException {
        newWindow(event, "MainView.fxml");
    }

    private boolean isValidCustomer(Customer customer) throws SQLException {
        if (nameTxt.getText().trim().isEmpty() || addressTxt.getText().trim().isEmpty()
                || phoneTxt.getText().trim().isEmpty() || cityCombo.getValue() == null) {
            System.out.println("EMPTY FIELDS");
            return false;
        }
        if (CustomerDatabase.getInstance().customerExists(customer)) {
            System.out.println("Customer already exists.");
            return false;
        }
        return true;
    }
}
