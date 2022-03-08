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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.countries;
import model.firstLevelDivison;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**The add customer class. */
public class AddCustomer implements Initializable {

    @FXML
    private ComboBox<firstLevelDivison> firstDivision;

    @FXML
    private ComboBox<countries> country;

    @FXML
    private TextField customerID;

    @FXML
    private TextField customerName;

    @FXML
    private TextField address;

    @FXML
    private TextField postalCode;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private static firstLevelDivison selectedDivision;

    ObservableList<firstLevelDivison> filteredDivisions = FXCollections.observableArrayList();
    /**The cancel customer method.
     * This method is for the cancel button action that changes scene.*/
    @FXML
    public void cancelCustomerAdd(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customer.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
    /**The save customer method.
     * This saves customer information into database when save button is clicked. */
    @FXML
    public void saveCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        selectedDivision = firstDivision.getSelectionModel().getSelectedItem();

        if(customerName.getText().isBlank() || address.getText().isBlank() || postalCode.getText().isBlank() || phoneNumber.getText().isBlank()){
            alertMessages.blankFieldError();
            return;
        }

        //INSERT DATA ON FORM INTO DATABASE THROUGH QUERY CLASS
        query.insertCustomer(customerName.getText(), address.getText(), postalCode.getText(), phoneNumber.getText(), selectedDivision.getDivisionID());



        Parent root = FXMLLoader.load(getClass().getResource("/view/customer.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
    /**The country combo box method.
     * This method fills country combo box*/
    @FXML
    public void countryComboBox(ActionEvent event) {
        int countryID = country.getSelectionModel().getSelectedItem().getCountryID();
        filteredDivisions.clear();
        for (firstLevelDivison fd : firstLevelDivison.getAllDivisons()) {
            if (countryID == fd.getCountryID()) {
                filteredDivisions.add(fd);
            }
        }
        firstDivision.setItems(filteredDivisions);
        firstDivision.getSelectionModel().selectFirst();

    }

    @FXML
    public void firstLevelDivComboBox(ActionEvent event) {
    }
    /**The initialize method.
     * This method initializes the scene and sets up information needed to be prefilled.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //SETTING UP COMBO BOXES
        ObservableList<countries> countryTable = countries.getAllCountries();
        ObservableList<firstLevelDivison> divisionTable = firstLevelDivison.getAllDivisons();

        country.setItems(countryTable);
        country.getSelectionModel().selectFirst();

        int countryID = country.getSelectionModel().getSelectedItem().getCountryID();

        for (firstLevelDivison fd : divisionTable) {
            if (countryID == fd.getCountryID()) {
                filteredDivisions.add(fd);
            }
        }
        firstDivision.setItems(filteredDivisions);
        firstDivision.getSelectionModel().selectFirst();

    }

}
