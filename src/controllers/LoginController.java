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
import repository.UserRepository;
import services.DatabaseConnection;
import services.PasswordHasher;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private Label loginMessageLabel;
    private UserRepository userRepository = new UserRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loginButtonOnAction(ActionEvent actionEvent) {
        if (!username.getText().isBlank() && !password.getText().isBlank()) {
            DatabaseConnection connectNow = DatabaseConnection.getConnection();
            String verifyLogin = "SELECT salted_password, salt FROM admin.users WHERE username = ?";
            try {
                DatabaseConnection connection = connectNow.getConnection();
                PreparedStatement pst = connection.prepareStatement(verifyLogin);
                pst.setString(1, username.getText());
                ResultSet queryResult = pst.executeQuery();

                if (queryResult.next()) {
                    String saltedPassword = queryResult.getString("salted_password");
                    String salt = queryResult.getString("salt");

                    if (saltedPassword == null) {
                        // Generate new salt
                        salt = PasswordHasher.generateSalt();
                        // Hash password with the new salt
                        saltedPassword = PasswordHasher.generateSaltedHash(password.getText(), salt);
                        // Update the database with the new salted password and salt
                        String updateQuery = "UPDATE admin.users SET salted_password = ?, salt = ? WHERE username = ?";
                        PreparedStatement updatePst = connection.prepareStatement(updateQuery);
                        updatePst.setString(1, saltedPassword);
                        updatePst.setString(2, salt);
                        updatePst.setString(3, username.getText());
                        updatePst.executeUpdate();
                    }

                    // Validate the entered password
                    String enteredSaltedPassword = PasswordHasher.generateSaltedHash(password.getText(), salt);
                    if (enteredSaltedPassword.equals(saltedPassword)) {
                        // Password is correct
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
                        // Invalid password
                        loginMessageLabel.setText("Invalid Login. Please try again.");
                    }
                } else {
                    // User does not exist
                    loginMessageLabel.setText("Invalid Login. Please try again.");
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
