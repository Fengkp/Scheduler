package model;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private int customerId;
    private int userId;
    private StringProperty appointmentType;
    private StringProperty customerName;
    private ObjectProperty<LocalDateTime> startTime;
    private ObjectProperty<LocalDateTime> endTime;

    public Appointment() {}

    public Appointment(String appointmentType,
                       LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentType = new SimpleStringProperty(appointmentType);
        this.startTime = new SimpleObjectProperty(startTime);
        this.endTime = new SimpleObjectProperty(endTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        userId = id;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName = new SimpleStringProperty(customerName);
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public String getAppointmentType() {
        return appointmentType.get();
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType.set(appointmentType);
    }

    public StringProperty appointmentTypeProperty() {
        return appointmentType;
    }

    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    public ObjectProperty<LocalDateTime> startTimeProperty() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    public ObjectProperty<LocalDateTime> endTimeProperty() {
        return endTime;
    }
}