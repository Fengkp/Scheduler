package view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Appointment;
import utils.AppointmentDatabase;
import utils.GetData;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportsController extends UniversalController {
    @FXML
    private Button appointmentTypesBtn, schedulesBtn, goBackBtn;

    public void goBackBtn(ActionEvent event) throws IOException {
        newWindow(event, "MainView.fxml");
    }

    public void appointmentTypesBtn() throws IOException, SQLException {
        List<String> appointmentTypes = new ArrayList<>();

        ResultSet results = GetData.getInstance().getDBResults("SELECT type, MONTHNAME(start) as 'Month', COUNT(*) "
                + "AS freq FROM appointment GROUP BY type, MONTH(start) ORDER BY Month(start)");
        while (results.next())
            appointmentTypes.add(results.getInt("freq") + " appointments of type " + results.getString("type")
                    + " in " + results.getString("Month"));
        GetData.getInstance().deleteFile("reports/appointmenttypes.txt");
        GetData.getInstance().outputToTxt("reports/appointmenttypes.txt", appointmentTypes);
    }

    public void schedulesBtn() throws SQLException, IOException {
        List<String> schedule = new ArrayList<>();
        String currentUser = "";
        String prevUser = "";

        ResultSet results = GetData.getInstance().getDBResults("SELECT customer.customerName, appointment.type, "
                + "appointment.start, appointment.end, appointment.createdBy FROM customer INNER JOIN appointment "
                + "ON customer.customerId = appointment.customerId  ORDER BY createdBy, start");
        results.next();
        prevUser = results.getString("createdBy");
        results.previous();
        while (results.next()) {
            currentUser = results.getString("createdBy");
            if (!currentUser.equals(prevUser)) {
                GetData.getInstance().outputToTxt("reports/" + prevUser + "Schedule.txt", schedule);
                schedule.clear();
            }
            schedule.add(results.getString("type") + " with " + results.getString("customerName")
                    + " from " + results.getTimestamp("start").toString() + " to "
                    + results.getTimestamp("end").toString());
            prevUser = currentUser;
        }
        GetData.getInstance().outputToTxt("reports/" + prevUser + "Schedule.txt", schedule);
    }
}
