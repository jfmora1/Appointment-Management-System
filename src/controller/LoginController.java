package controller;

import helper.query;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.appointment;
import model.users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
/**The Login Controller Class. */
public class LoginController implements Initializable {

    public PasswordField password;
    public Label invalid_login;
    public TextField username;
    public Label userLocation;
    public Button login_button;
    public Label usernameLabel;
    public Label passwordLabel;
    private int userLoggedIn;

    /**The initialize method.
     * Initializes the scene. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLocation.setText(ZoneId.systemDefault().toString());

        ResourceBundle rb = ResourceBundle.getBundle("helper.language", Locale.getDefault());
        usernameLabel.setText(rb.getString("usernameLabel"));
        passwordLabel.setText(rb.getString("passwordLabel"));
        login_button.setText(rb.getString("login_button"));
        username.setPromptText(rb.getString("promptUser"));
        password.setPromptText(rb.getString("promptPass"));

    }
    /**User login method.
     * Validates user trying to login when login button is clicked.*/
    public void userlogin(ActionEvent event) throws IOException, SQLException {
        query.loginUser(username.getText(), password.getText());
        if(query.getSuccessLogin()){
            checkForApp();
        }

    }
    /**Check for appointment method.
     * checks for appointments within 15 minute window*/
    public void checkForApp() throws SQLException {
        //FIND USER LOGGED IN
        for(users u : query.getUserName()){
            if(query.getUserLoggedIn().equals(u.getUserName())){
                userLoggedIn = u.getUser_id();
            }else if(query.getUserLoggedIn() == null){
                return;
            }
        }

        ObservableList<appointment> appointment15min = query.getApp15Min(userLoggedIn);


        if (!appointment15min.isEmpty()) {
            for (appointment a : appointment15min) {
                String text = "You have Appointment " + a.getAppointmentID() + " for " + a.getStartTime();
                alertMessages.appointment15min(text);
            }
        }else{
            String text = "You have no upcoming appointments in the next 15 minutes.";
            alertMessages.appointment15min(text);
        }
    }

}
