package view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.City;
import model.Customer;
import utils.AppointmentDatabase;
import utils.CustomerDatabase;
import java.io.IOException;
import java.sql.SQLException;

public class NewCustomerController extends UniversalController {
    @FXML
    private TextField nameTxt, addressTxt, phoneTxt;
    @FXML
    private Button confirmBtn, cancelBtn, deleteBtn;
    @FXML
    private ComboBox<City> cityCombo;
    @FXML
    private ComboBox<Customer> customerCombo;

    private Customer selectedCustomer;
    private boolean editCustomer = false;

    @FXML
    public void initialize() {
        deleteBtn.setDisable(true);
        cityCombo.setItems(CustomerDatabase.getInstance().getCities());
        customerCombo.setItems(CustomerDatabase.getInstance().getCustomers());
        customerCombo.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        editCustomer = true;
                        deleteBtn.setDisable(false);
                        selectedCustomer = customerCombo.getValue();
                        nameTxt.setText(selectedCustomer.getName());
                        addressTxt.setText(selectedCustomer.getAddress());
                        phoneTxt.setText(selectedCustomer.getPhone());
                        cityCombo.setValue(selectedCustomer.getCity());
                    }
                });
    }

    public void confirmBtn(ActionEvent event) throws IOException, SQLException {
        Customer customer = new Customer(nameTxt.getText(), addressTxt.getText(), cityCombo.getValue(), phoneTxt.getText());

        if (isValidCustomer(customer)) {
            if (editCustomer)
                CustomerDatabase.getInstance().addCustomer(customer, selectedCustomer.getId());
            else
                CustomerDatabase.getInstance().addCustomer(customer, -1);
            CustomerDatabase.getInstance().refreshCustomers();
            AppointmentDatabase.getInstance().refreshAppointments();
            cancelBtn(event);
        }
        else
            System.out.println("Could not add or edit customer.");
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
        if (CustomerDatabase.getInstance().customerExists(customer) && !editCustomer) {
            System.out.println("Customer already exists.");
            return false;
        }
        return true;
    }

    public void deleteBtn(ActionEvent event) throws SQLException, IOException {
        CustomerDatabase.getInstance().deleteCustomer(selectedCustomer.getId());
        CustomerDatabase.getInstance().refreshCustomers();
        cancelBtn(event);
    }
}
