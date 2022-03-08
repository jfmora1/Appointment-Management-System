package controller;

import helper.query;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;
import model.customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**The Customer Controller Class. */
public class Customer implements Initializable {
    public TableView<customer> customerTable;
    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableColumn addressColumn;
    public TableColumn postalcodeColumn;
    public TableColumn firstlevelColumn;
    public TableColumn CountryColumn;
    public Button homeButton;
    public Button deleteButton;
    public Button modifyButton;
    public Button addButton;
    private static customer selectedCustomer;

    public static customer getSelectedCustomer() {
        return selectedCustomer;
    }
    /**The initialize method.
     * Initializes the scene.
     * The lambda expression being used changes the scene when the add button is clicked. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        CREATING LIST TO HOLD COUNTRY INFO
        ObservableList<customer> cm = query.getAllCustomerinfo();

//        //SET COUNTRY LIST INSIDE CUSTOMER TABLE
        customerTable.setItems(cm);
//
//        //INITIALIZING COLUMNS
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        postalcodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalcode"));
        firstlevelColumn.setCellValueFactory(new PropertyValueFactory<>("firstlevelDivision"));
        CountryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));

        //LAMBDA EXPRESSION THAT CHANGES SCENE WHEN ADD BUTTON CLICKED
        addButton.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Appointment Management");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
                }

        );
    }
    /**Home button action method. */
    public void homeButtonAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
    /**Modify button action method. */
    public void modifybuttonAction(ActionEvent actionEvent) throws IOException {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/modifyCustomer.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
    /**Delete button action method. */
    public void deletebuttonAction(ActionEvent actionEvent) throws SQLException {
        customer cm = customerTable.getSelectionModel().getSelectedItem();
        int customerID = cm.getCustomerID();

        if (cm == null) {
            return;
        }

        for (appointment a : query.getAllAppointmentInfo()) {
            if (customerID == a.getCustomerID()) {
                alertMessages.invalidCustomerDelete();
                return;
            }
        }
            if (alertMessages.deleteConfirmationCustomer() == true) {
                query.deleteCustomer(cm.getCustomerID());
                alertMessages.deletedCM(cm.getCustomerName());
                customerTable.setItems(query.getAllCustomerinfo());
            }
        }
    }
