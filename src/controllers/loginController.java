package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.util.EventObject;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private Button login;
    @FXML
    private Button cancel;
    @FXML
    private EventObject event;
    @FXML
    private Label loginMessageLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    // ...

    public void loginButtonOnAction(ActionEvent actionEvent) {
        if (!username.getText().isBlank() && !password.getText().isBlank()) {
            DatabaseHandler connectNow = new DatabaseHandler();
            String verifyLogin = "SELECT password FROM admin.users WHERE username = '" + username.getText() + "'";
            try {
                ResultSet queryResult = connectNow.execQuery(verifyLogin);

                if (queryResult.next()) {
                    String hashedPasswordFromDB = queryResult.getString("password");
                    String enteredPassword = password.getText();

                    if (BCrypt.checkpw(enteredPassword, hashedPasswordFromDB)) {
                        // Successful login
                        // Add your code here to proceed with the login process

                        // Call the displaySavedCredentials() method
                        displaySavedCredentials();

                        Locale currentLocale = Locale.getDefault();
                        Locale locale = new Locale("sq_AL");
                        ResourceBundle bundle = ResourceBundle.getBundle("/Properties/Language", locale);
                        Parent root = FXMLLoader.load(getClass().getResource("/resources/View/main.fxml"), bundle);
                        Scene scene = new Scene(root);
                        Stage primaryStage = (Stage) username.getScene().getWindow();
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("Library Management System");
                        primaryStage.show();
                    } else {
                        loginMessageLabel.setText("Invalid Login. Please try again.");
                    }
                } else {
                    loginMessageLabel.setText("Invalid Login. Please try again.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            loginMessageLabel.setText("Please enter username and password.");
        }
    }

    public void displaySavedCredentials() {
        DatabaseHandler connectNow = new DatabaseHandler();
        String verifyLogin = "SELECT username, password FROM admin.users";
        try {
            ResultSet queryResult = connectNow.execQuery(verifyLogin);

            while (queryResult.next()) {
                String savedUsername = queryResult.getString("username");
                String savedPassword = queryResult.getString("password");
                System.out.println("Username: " + savedUsername + ", Password: " + savedPassword);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void cancelButtonOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}



