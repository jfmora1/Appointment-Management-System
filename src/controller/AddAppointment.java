package controller;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import model.users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
/**The add appointment class*/
public class AddAppointment implements Initializable {

    public ComboBox<customer> customerIdBox;
    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox fromTime;

    @FXML
    private ComboBox toTime;

    @FXML
    private ComboBox<contacts> contact;

    @FXML
    private TextField appointmentID;

    @FXML
    private TextField title;

    @FXML
    private TextField description;

    @FXML
    private TextField location;

    @FXML
    private TextField type;

    @FXML
    private TextField userID;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private int userLoggedIn;

    private ZoneId myZoneID = ZoneId.of(TimeZone.getDefault().getID());


    /** The initialize method for add appointment scene.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //CREATE LIST TO SET TIMES INTO COMBO BOX FOR APPOINTMENT TIMES
        ObservableList<String> list = FXCollections.observableArrayList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        fromTime.setItems(list);
        fromTime.getSelectionModel().selectFirst();

        //RETRIEVE ALL CONTACTS AND SET INTO COMBO BOX
        ObservableList<contacts> contactinfo = contacts.getAll_contacts();
        contact.setItems(contactinfo);
        contact.getSelectionModel().selectFirst();

        //FIND USER LOGGED IN AND SET AS USER_ID TEXT FIELD
        for (users u : query.getUserName()) {
            if (query.getUserLoggedIn().equals(u.getUserName())) {
                userLoggedIn = u.getUser_id();
            }
        }
        userID.setText(String.valueOf(userLoggedIn));

        //SET CUSTOMER COMBO BOX
        setCustomerBox();

    }
    /** The from selected time method.
     * This method grabs the time from combo box and puts it into a String variable.*/
    public void fromSelectedtime(javafx.event.ActionEvent actionEvent) {
        String s = fromTime.getSelectionModel().getSelectedItem().toString();
    }
    /**This method sets the customer combo box information. */
    public void setCustomerBox() {
        ObservableList<customer> c = customer.getAllCustomers();
        customerIdBox.setItems(c);
        customerIdBox.getSelectionModel().selectFirst();
    }
    /**The validate eastern time method.
     * This method validates local time to eastern business hours time.
     * @param start Brings in the time for that start of the appointment
     * @param date Brings in the start for the appointment date*/
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
    /**This is the save appointment method.
     * This method is an action for the save button is clicked and saves the appointment information.*/
    public void saveAppointment(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        DateTimeFormatter formatTimeDate = DateTimeFormatter.ofPattern("HH:mm");
        //RETRIEVE CONTACT SELECTION
        contacts c = contact.getSelectionModel().getSelectedItem();
        customer cm = customerIdBox.getSelectionModel().getSelectedItem();

        if (title.getText().isBlank() || description.getText().isBlank() || location.getText().isBlank() ||
                type.getText().isBlank() || datePicker.getValue() == null) {
            alertMessages.blankFieldError();
            return;
        }

        //RETRIEVE TIME AND DATE AND FORMAT FOR DB INSERT
        LocalDateTime startApp = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(fromTime.getValue().toString(), formatTimeDate));
        ZonedDateTime startappUTC = ZonedDateTime.of(startApp, myZoneID);

        //VALIDATES LOCAL TIME WITH EASTERN BUSINESS HOURS TIME
        if(!validateEasternTime(startappUTC,datePicker.getValue())){
            String text = "Invalid start time for appointment \nnot within business hours";
            alertMessages.appointment(text);
            return;
        }

        //FORMATS TIME TO UTC FOR DB INSERT
        //DON'T NEED FOR VM
        startappUTC = startappUTC.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endappUTC = startappUTC.plusHours(1);


//        testing ovelapping appointment check
        if(!validateOvelap(cm.getCustomerID(),startappUTC)){
            String text = "APPOINTMENT OVERLAP \nPlease change time/date of appointment.";
            alertMessages.appointment(text);
            return;
        }

        query.insertAppointment(title.getText(), description.getText(), location.getText(), type.getText(), cm.getCustomerID(), Integer.parseInt(userID.getText()), c.getContact_id(), startappUTC, endappUTC);

        Parent root = FXMLLoader.load(getClass().getResource("/view/appointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }

    /**This is the validateoverlap method.
     * This method validates there is no appointment overlap between customers
     * @param customerID Customer ID
     * @param appointmentTime appointment time to validate
     * */
    public Boolean validateOvelap(int customerID, ZonedDateTime appointmentTime) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime ldt1 = appointmentTime.toLocalDateTime();
        ObservableList<appointment> allApps = query.getOverlapAppointments(customerID,ldt1);
        for(appointment app : allApps){
            LocalDateTime dateTime = LocalDateTime.parse(app.getStartTime(), formatter);
            if(dateTime.equals(ldt1)){
                return false;
            }
        }
        return true;
    }
    /**This is the cancel appointment Method.
     * This is for the button cancel action that sends you to appointment scene.*/
    public void cancelAppointmentCreate(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/appointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
}

