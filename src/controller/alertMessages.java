package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the Alert class for error's in controllers.
 */
public class alertMessages {

    /**
     * deleteConfirmation Method.
     * Alerts user to confirm to delete an appointment
     */
    public static boolean deleteConfirmationApp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Appointment");
        alert.setHeaderText("Delete");
        alert.setContentText("Do you want to delete this appointment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
    /**Delete confirmation customer method.
     * Alerts users to confirm deletion of customer*/
    public static boolean deleteConfirmationCustomer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Customer");
        alert.setHeaderText("Delete");
        alert.setContentText("Do you want to delete this customer?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
    /**The delete customer method.
     * Alert informing of customer being deleted.
     * @param cm customer being deleted*/
    public static void deletedCM(String cm){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customer");
        alert.setHeaderText("CONFIRMED");
        alert.setContentText("Customer " + cm + " was deleted.");
        alert.showAndWait();
    }
    /**Delete appointment method.
     * Alert informing of what appointment is being deleted.
     * @param id appointment id
     * @param type Appointment type*/
    public static void deletedAPP(int id, String type){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointments");
        alert.setHeaderText("CONFIRMED");
        alert.setContentText("Appointment "+ id + " " + type + " was canceled.");
        alert.showAndWait();
    }
    /**Invalid username method.
     * Alert for invalid username login*/
    public static void invalidUsername() {
        ResourceBundle rb = ResourceBundle.getBundle("helper.language", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(rb.getString("error"));
        alert.setContentText(rb.getString("invalidUsername"));
        alert.showAndWait();
    }
    /**Invalid password method.
     * Alert for invalid password.*/
    public static void invalidPassword() {
        ResourceBundle rb = ResourceBundle.getBundle("helper.language", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(rb.getString("error"));
        alert.setContentText(rb.getString("invalidPassword"));
        alert.showAndWait();
    }
    /**Invalid customer method.*/
    public static void invalidCustomerDelete(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("ERROR");
        alert.setContentText("Unable to delete, all appointments attached \nto customer must be deleted first.");
        alert.showAndWait();
    }
    /**Blank field error method. */
    public static void blankFieldError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("WARNING");
        alert.setContentText("Please make sure all fields are completed");
        alert.showAndWait();
    }
    /**Appointment15min method.
     * Alerts for an appointment in 15 minutes after login*/
    public static void appointment15min(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Appointment");
        alert.setContentText(text);
        alert.showAndWait();
    }
    /**Appointment method.
     * Alerts for appointment errors
     * @param text text for alert*/
    public static void appointment(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Appointment");
        alert.setContentText(text);
        alert.showAndWait();
    }

}
