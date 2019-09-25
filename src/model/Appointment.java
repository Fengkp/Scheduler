package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.GetData;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Appointment {

    private String id;
    private String customerId;
    private StringProperty appointmentType;
    private StringProperty customerName;
    private ObjectProperty<LocalDateTime> startTime;
    private ObjectProperty<LocalDateTime> endTime;

    public Appointment() {}

    public Appointment(String id, String customerId, String appointmentType,
                       LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        this.id = id;
        this.customerId = customerId;
        this.customerName = new SimpleStringProperty(GetData.getCustomerName(customerId));
        this.appointmentType = new SimpleStringProperty(appointmentType);
        this.startTime = new SimpleObjectProperty(startTime);
        this.endTime = new SimpleObjectProperty(endTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
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
