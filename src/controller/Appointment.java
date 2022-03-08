package controller;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;
/**The Appointment Controller class*/
public class Appointment implements Initializable {

    public TableView<appointment> appointmentTable;
    @FXML
    private TableColumn<appointment,Integer> idColumn;
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn descriptionColumn;
    @FXML
    private TableColumn locationColumn;
    @FXML
    private TableColumn contactColumn;
    @FXML
    private TableColumn starttimeColumn;
    @FXML
    private TableColumn endtimeColumn;
    @FXML
    private TableColumn customerIdColumn;
    @FXML
    private TableColumn userIdColumn;
    @FXML
    public TableColumn typeColumn;
    @FXML
    private Button homeButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Button addButton;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private ToggleGroup filterAppts;
    @FXML
    private RadioButton monthRadioButton;
    public RadioButton allRadioButton;
    private static appointment selectedAppointment;
    ZoneId myZoneID = ZoneId.of(TimeZone.getDefault().getID());

    public static appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    /**Add button action method. */
    @FXML
    public void addbuttonAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();

    }
    /**Home button action method. */
    @FXML
    public void homeButtonAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();

    }
    /**The modify button action method. */
    @FXML
    public void modifybuttonAction(ActionEvent actionEvent) throws IOException {
        selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/modifyAppointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This method deletes the appointment selected/highlighted.
     */
    @FXML
    public void deletebuttonAction(ActionEvent event) throws SQLException {
        appointment ap = appointmentTable.getSelectionModel().getSelectedItem();
        if(ap == null){
            return;
        }
        if(alertMessages.deleteConfirmationApp() == true){
            query.deleteAppointment(ap.getAppointmentID());
            alertMessages.deletedAPP(ap.getAppointmentID(),ap.getType());
            appointmentTable.setItems(query.getAllAppointmentInfo());
        }


    }
    /**Month filter appointment method.
     * This method filters appointments by month.*/
    @FXML
    public void monthFilterAppts(ActionEvent event) throws SQLException {
        ObservableList<appointment> monthAppointments = FXCollections.observableArrayList();
        ZonedDateTime start = ZonedDateTime.now(myZoneID);
        ZonedDateTime end = start.plusMonths(1);

        ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

        monthAppointments = query.getFilteredAppointments(startUTC,endUTC);
        appointmentTable.setItems(monthAppointments);

    }
    /**Week filter appointment method.
     * This method filters appointments by week.*/
    @FXML
    public void weekFilterAppts(ActionEvent event) throws SQLException {
        ObservableList<appointment> weekAppointments = FXCollections.observableArrayList();
        ZonedDateTime start = ZonedDateTime.now(myZoneID);
        ZonedDateTime end = start.plusWeeks(1);

        ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

        weekAppointments = query.getFilteredAppointments(startUTC,endUTC);
        appointmentTable.setItems(weekAppointments);

    }
    /**The initialize method.
     * Initializes the scene.
     * The lambda being used helps set all appointments when the ALL filter radio button is clicked.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<appointment> appointmentInfo = query.getAllAppointmentInfo();

        appointmentTable.setItems(appointmentInfo);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        starttimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endtimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //LAMBDA EXPRESSION THAT SETS ALL APPOINTMENTS WHEN ALL FILTER RADIO IS CLICKED
        allRadioButton.setOnAction(e -> appointmentTable.setItems(appointmentInfo));
    }


}

