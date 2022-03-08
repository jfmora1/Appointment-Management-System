package controller;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.appointment;
import model.contacts;
import model.customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
/**Modify Appointment class. */
public class ModifyAppointment implements Initializable {
    public DatePicker datePicker;
    public ComboBox fromTime;
    public ComboBox<contacts> contact;
    public TextField appointmentID;
    public TextField title;
    public TextField description;
    public TextField location;
    public TextField type;
    public Button saveButton;
    public Button cancelButton;
    public ComboBox<customer> customerCbox;
    public TextField userID;
    private appointment selectedAppointment;
    private ZoneId myZoneID = ZoneId.of(TimeZone.getDefault().getID());

    /**Initialize method. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //RETRIEVING AND SETTING INFO FROM SELECTED APPOINTMENT
        selectedAppointment = Appointment.getSelectedAppointment();
        appointmentID.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        title.setText(String.valueOf(selectedAppointment.getTitle()));
        description.setText(String.valueOf(selectedAppointment.getDescription()));
        location.setText(String.valueOf(selectedAppointment.getLocation()));
        type.setText(String.valueOf(selectedAppointment.getType()));
        userID.setText(String.valueOf(selectedAppointment.getUserID()));
        datePicker.setValue(LocalDate.parse(selectedAppointment.getStartTime(),format));

        //SET APPOINTMENT TIMES
        ObservableList<String> list = FXCollections.observableArrayList("00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00", "09:00", "10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00");
        fromTime.setItems(list);
        fromTime.getSelectionModel().select(LocalTime.parse(selectedAppointment.getStartTime(),format));

        //Set contacts & customers to combo boxes
        setContactBox(selectedAppointment.getContact());
        setCustomerBox(selectedAppointment.getCustomerID());

    }

    public void setContactBox(String c){
        ObservableList<contacts> contactinfo = contacts.getAll_contacts();
        contact.setItems(contactinfo);
        for(contacts ct : contactinfo){
            if(c.equals(ct.getContact_name())) {
                contact.getSelectionModel().select(ct);
            }
        }
    }

    public void setCustomerBox(int id){
        ObservableList<customer> c = customer.getAllCustomers();
        customerCbox.setItems(c);
        for(customer cm : c){
            if(id == cm.getCustomerID())
                customerCbox.getSelectionModel().select(cm);
        }
    }

    public void fromSelectedtime(ActionEvent actionEvent) {
    }

    /**Validate est time method.
     * Validates business time to locoal time.
     * @param date date of appointment
     * @param start start time of appointment*/
    public Boolean validateEasternTime(ZonedDateTime start, LocalDate date){
        ZoneId estZone = ZoneId.of("America/New_York");
        ZonedDateTime end = start.plusHours(1);
        ZonedDateTime startEstTime = ZonedDateTime.of(date,LocalTime.of(8,0),estZone);
        ZonedDateTime endEstTime = ZonedDateTime.of(date,LocalTime.of(22,0),estZone);

        if(start.isBefore(startEstTime) | start.isAfter(endEstTime) |end.isBefore(startEstTime) | end.isAfter(endEstTime)){
            return false;
        }else{
            return true;
        }
    }
    /**Save appointment method.
     * Saves appointment into database.*/
    public void saveAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        customer cm = customerCbox.getSelectionModel().getSelectedItem();
        contacts c = contact.getSelectionModel().getSelectedItem();

        if(title.getText().isBlank() || description.getText().isBlank() || location.getText().isBlank() ||
                type.getText().isBlank() || datePicker.getValue() == null){
            alertMessages.blankFieldError();
            return;
        }




        //RETRIEVE TIME AND DATE AND FORMAT FOR DB INSERT
        DateTimeFormatter formatTimeDate = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime startApp = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(fromTime.getValue().toString(),formatTimeDate));
        ZonedDateTime startappZone = ZonedDateTime.of(startApp, myZoneID);

        //VALIDATES LOCAL TIME WITH EASTERN BUSINESS HOURS TIME
        if(!validateEasternTime(startappZone,datePicker.getValue())){
            String text = "Invalid start time for appointment\nnot within business hours";
            alertMessages.appointment(text);
            return;
        }

        ZonedDateTime startappUTC = startappZone.withZoneSameInstant(ZoneOffset.UTC);

//        testing ovelapping appointment check
        if(!validateOvelap(cm.getCustomerID(),startappUTC)){
            String text = "APPOINTMENT OVERLAP \nPlease change time/date of appointment.";
            alertMessages.appointment(text);
            return;
        }

        LocalDateTime start = startappUTC.toLocalDateTime();
        Timestamp tm = Timestamp.valueOf(start);
        LocalDateTime endApp = start.plusHours(1);
        Timestamp tm2 = Timestamp.valueOf(endApp);

        query.updateAppointment(Integer.parseInt(appointmentID.getText()),title.getText(),description.getText(),location.getText(),type.getText(),tm,tm2,cm.getCustomerID(),Integer.parseInt(userID.getText()),c.getContact_id());

        Parent root = FXMLLoader.load(getClass().getResource("/view/appointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }

    /**Validate overlap method.
     * Validates there is no overlap in appointment times and dates
     * @param appointmentTime appointment time
     * @param customerID customer ID*/
    public Boolean validateOvelap(int customerID, ZonedDateTime appointmentTime) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime ldt1 = appointmentTime.toLocalDateTime();
        ObservableList<appointment> allApps = query.getOverlapAppointments(customerID,ldt1);
        for(appointment app : allApps){
            int appID = app.getAppointmentID();
            LocalDateTime dateTime = LocalDateTime.parse(app.getStartTime(), formatter);
            if(dateTime.equals(ldt1) & appID != selectedAppointment.getAppointmentID()){
                return false;
            }
        }
        return true;
    }
    /**Cancel appointment create method. */
    public void cancelAppointmentCreate(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/appointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }


}
