package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
/** This is the Main class of the application. */
public class Main extends Application {
    private static Stage stg;
/** This is the first stage that loads.*/
    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("helper/language", Locale.getDefault());
        stg = stage;
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setTitle(rb.getString("appointmentMan"));
        stage.setScene(new Scene(root));
        stage.show();
    }
    /** This changeScene method.
     * This method changes scenes that are passed onto.
     * @param fxml The scene it is being changed too.*/
    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }
    /** This is the main method.
     * This is the starting of the application and is responsible for starting and closing the database connection. */
    public static void main(String[] args) throws SQLException {
        //This next line of code is to switch to french for login form test
//        Locale.setDefault(new Locale("fr"));
        JDBC.startConnection();

        launch(args);

        JDBC.closeConnection();


    }
}
