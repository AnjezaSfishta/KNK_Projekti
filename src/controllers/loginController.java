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
import services.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.util.EventObject;
import java.util.Locale;
import java.util.ResourceBundle;

public class loginController  implements Initializable {

@FXML
    private PasswordField password;
@FXML
    private TextField username;
@FXML
private Button login;
@FXML
private Button cancel;
    private EventObject event;
    @FXML
    private Label loginMessageLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




    public void loginButtonOnAction(ActionEvent actionEvent) {
        if(!username.getText().isBlank() && !password.getText().isBlank()) {
                // loginMessageLabel.setText("You try to login!");
                DatabaseHandler connectNow = new DatabaseHandler();
                String verifyLogin = "select count(1) from admin.users where username = '" + username.getText() + "' and password = '" + password.getText() + "'";
                try {

                    ResultSet queryResult = connectNow.execQuery(verifyLogin);

                    while(queryResult.next()) {
                        if(queryResult.getInt(1) == 1) {
                            //loginMessageLabel.setText("Welcome!");
                            Locale currentLocale = Locale.getDefault();
                            Locale locale = new Locale("sq_AL");
                            ResourceBundle boundle = ResourceBundle.getBundle("/Properties/Language", locale);
                            Parent root = FXMLLoader.load(getClass().getResource("/resources/View/main.fxml"),boundle);
                            Scene scene = new Scene(root);
                            Stage primaryStage=new Stage();
                            primaryStage = (Stage) username.getScene().getWindow();
                            primaryStage.setScene(scene);
                            primaryStage.setTitle("Library Management System");
                            primaryStage.show();
                        } else {
                            loginMessageLabel.setText("Invalid Login. Please try again.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                loginMessageLabel.setText("Please enter username and password.");

            }
    }


    public void cancelButtonOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
