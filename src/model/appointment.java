package model;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Appointment model class. */
public class appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String startTime;
    private String endTime;
    private int customerID;
    private int userID;
    private int total;

    private static ObservableList<appointment> allAppointments = FXCollections.observableArrayList();

    /**Appointment method.
     * Sets appointment variables*/
    public appointment(int appointmentID, String title, String description, String location, String type, String contact, String startTime, String endTime, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.contact = contact;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
    }

    public appointment(String month,String type,int total){
        this.startTime = month;
        this.type = type;
        this.total = total;
    }

    public appointment(String month, int total) {
        this.startTime = month;
        this.total = total;
    }

    static {
        init();
    }

    private static void init() {
        if(allAppointments.size() == 0){
            ObservableList<appointment> allinfo = query.getAllAppointmentInfo();
            allAppointments.addAll(allinfo);
        }
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static ObservableList<appointment> getAllAppointments() {
        return allAppointments;
    }
}
