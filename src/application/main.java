package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.DatabaseHandler;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Locale currentLocale = Locale.getDefault();
        Locale locale = new Locale("sq_AL");
        ResourceBundle boundle = ResourceBundle.getBundle("/properties/Language", locale);
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"), boundle);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        DatabaseHandler.getInstance();       //instantiate databazen nga ketu, per ma shpejt me punu me butonat kur i shtypim.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
