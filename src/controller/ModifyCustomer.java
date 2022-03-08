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
import model.customer;
import model.firstLevelDivison;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**Modify customer class. */
public class ModifyCustomer implements Initializable {

    @FXML
    private ComboBox<firstLevelDivison> firstLevelDivision;
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
    private customer selectedCustomer;
    private static firstLevelDivison selectedDivision;
    ObservableList<firstLevelDivison> filteredDivisions = FXCollections.observableArrayList();
    /**Save customer method.
     * Saves customer into database. */
    @FXML
    void saveCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        //GET SELECTED DIVISION
        selectedDivision = firstLevelDivision.getSelectionModel().getSelectedItem();

        if(customerName.getText().isBlank() || address.getText().isBlank() || postalCode.getText().isBlank() || phoneNumber.getText().isBlank()) {
            alertMessages.blankFieldError();
            return;
        }

        //SAVE UPDATED CUSTOMER INFORMATION INTO DB
        int rowsAffected = query.updateCustomer(Integer.parseInt(customerID.getText()),customerName.getText(),address.getText(),postalCode.getText(),phoneNumber.getText(),selectedDivision.getDivisionID());
        if(rowsAffected > 0){
            System.out.println("Update customer successful");
        }else{
            System.out.println("Update customer failed");
        }


        Parent root = FXMLLoader.load(getClass().getResource("/view/customer.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();

    }
    /**Cancel customer add method. */
    @FXML
    void cancelCustomerAdd(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customer.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();

    }
    /**Country combo box method.
     * Fills country combo box*/
    @FXML
    void countryComboBox(ActionEvent event) throws SQLException {
        int countryID = country.getSelectionModel().getSelectedItem().getCountryID();
        filteredDivisions.clear();
        for (firstLevelDivison fd : firstLevelDivison.getAllDivisons()) {
            if (countryID == fd.getCountryID()) {
                filteredDivisions.add(fd);
            }
        }
        firstLevelDivision.setItems(filteredDivisions);
        firstLevelDivision.getSelectionModel().selectFirst();
    }

    @FXML
    void firstLevelDivComboBox(ActionEvent event) {

    }

    /**Initialize method. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set customer info
        selectedCustomer = Customer.getSelectedCustomer();
        String c = selectedCustomer.getCountry();
        int d = selectedCustomer.getDivisionID();

        customerID.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerName.setText(String.valueOf(selectedCustomer.getCustomerName()));
        address.setText(String.valueOf(selectedCustomer.getAddress()));
        postalCode.setText(String.valueOf(selectedCustomer.getPostalcode()));
        phoneNumber.setText(String.valueOf(selectedCustomer.getPhoneNumber()));

        //set combo boxes
        ObservableList<countries> countryTable = countries.getAllCountries();
        ObservableList<firstLevelDivison> divisionTable = firstLevelDivison.getAllDivisons();

        country.setItems(countryTable);
        for(countries cm : countryTable) {
            if (c.equals(cm.getCountry())){
                country.getSelectionModel().select(cm);
            }
        }

        int countryID = country.getSelectionModel().getSelectedItem().getCountryID();

        for (firstLevelDivison fd : divisionTable) {
            if (countryID == fd.getCountryID()) {
                filteredDivisions.add(fd);
            }
        }
        firstLevelDivision.setItems(filteredDivisions);
        for(firstLevelDivison fd : divisionTable){
            if(d == fd.getDivisionID()){
                firstLevelDivision.getSelectionModel().select(fd);
            }
        }



    }

}
