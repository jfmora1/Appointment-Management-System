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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;
import model.contacts;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**Reports controller class. */
public class Reports implements Initializable {
    public TableView tableviewbyType;
    public TableView tableViewByContact;
    public TableView tableViewByMonth;
    @FXML
    private Button homeButton;

    @FXML
    private ComboBox<contacts> contactBox;

    @FXML
    private TableColumn month1;

    @FXML
    private TableColumn typeOfAppointment;

    @FXML
    private TableColumn TotalNumberAppointments1;

    @FXML
    private TableColumn appointmentID;

    @FXML
    private TableColumn title;

    @FXML
    private TableColumn type;

    @FXML
    private TableColumn description;

    @FXML
    private TableColumn startTime;

    @FXML
    private TableColumn endTime;

    @FXML
    private TableColumn customerID;

    @FXML
    private TableColumn month2;

    @FXML
    private TableColumn totalAppointments2;
    ObservableList<appointment> filteredAppointments = FXCollections.observableArrayList();
    /**ContactComboBox method.
     * Fills country combo box information. */
    @FXML
    void contactComboBox(ActionEvent event) {
        //WHEN SELECTING A CONTACT, TABLEVIEW CLEARS AND INSERTS APPOINTMENTS WITH THAT SPECIFIC CONTACT
        contacts c = contactBox.getSelectionModel().getSelectedItem();
        filteredAppointments.clear();
        ObservableList<appointment> app = FXCollections.observableArrayList();
        app = query.getAllAppointmentInfo();

        for (appointment a : app) {
            if(c.getContact_name().equals(a.getContact())){
                filteredAppointments.add(a);
            }
        }
        tableViewByContact.setItems(filteredAppointments);
    }

    public void homeButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
    /**The initialize method. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //RETRIEVE ALL CONTACTS AND SET INTO COMBO BOX
        ObservableList<contacts> contactinfo = contacts.getAll_contacts();
        contactBox.setItems(contactinfo);
        contactBox.getSelectionModel().selectFirst();

        //SETTING UP 1ST REPORTS TABLEVIEW
        ObservableList<appointment> appointmentInfo = FXCollections.observableArrayList();
        try {
            appointmentInfo = query.getAppointmentByType();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tableviewbyType.setItems(appointmentInfo);
        month1.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        typeOfAppointment.setCellValueFactory(new PropertyValueFactory<>("type"));
        TotalNumberAppointments1.setCellValueFactory(new PropertyValueFactory<>("total"));

        //SETTING UP 2ND REPORT TABLEVIEW
        contacts c = contactBox.getSelectionModel().getSelectedItem();
        ObservableList<appointment> app = FXCollections.observableArrayList();
        app = query.getAllAppointmentInfo();

        for (appointment a : app) {
            if (a.getContact().equals(c.getContact_name())) {
                filteredAppointments.add(a);
            }
        }
        tableViewByContact.setItems(filteredAppointments);

        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        //SETTING UP 3RD REPORT TABLEVIEW
        ObservableList<appointment> tableView3 = FXCollections.observableArrayList();
        try {
            tableView3 = query.getAppointmentByMonth();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tableViewByMonth.setItems(tableView3);
        month2.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        totalAppointments2.setCellValueFactory(new PropertyValueFactory<>("total"));

    }

}
