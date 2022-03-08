package controller;

import helper.query;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
/**The Mainscreen class. */
public class MainScreen implements Initializable {
    public Button customerButton;
    public Button reportsButton;
    public Button appointmentButton;
    public Button logoutButton;
    private int userLoggedIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**Customer page action method. */
    public void CustomerPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customer.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }

    public void reportPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
    /**Appointment page action method. */
    public void appointmentPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/appointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
    /**User logout method.
     * Logs out the user*/
    public void userLogout(ActionEvent actionEvent) throws IOException {
        query.setSuccessfulLogin(false);
        ResourceBundle rb = ResourceBundle.getBundle("helper/language", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(rb.getString("appointmentMan"));
        stage.setScene(scene);
        stage.show();

    }
}
