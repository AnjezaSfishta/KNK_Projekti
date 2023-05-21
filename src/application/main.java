package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Pagination;
import services.DatabaseHandler;

import java.io.IOException;
import java.util.ArrayList;

public class main extends Application {



    @Override
    public void start(Stage primaryStage) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("/View/logins.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        DatabaseHandler.getInstance();       //instantiate databazen nga ketu, per ma shpejt me punu me butonat kur i shtypim.
        Pagination pagination = new Pagination(2, 10);

        String sqlQuery = pagination.getSQLQuery();
        System.out.println("SQL Query: " + sqlQuery);

        ArrayList<Object> sqlParams = pagination.getSQLParams();
        System.out.println("SQL Parameters: " + sqlParams);
    }


    public static void main(String[] args) {
        launch(args);


    }

}
