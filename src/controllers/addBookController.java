package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import repositories.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class addBookController implements Initializable {

    @FXML
    private TextField author;

    @FXML
    private Button cancel;

    @FXML
    private TextField id;

    @FXML
    private TextField publisher;

    @FXML
    private Button save;

    @FXML
    private TextField title;

    @FXML
    void addBook(ActionEvent event) {

    }


    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    void cancel(ActionEvent event) {

    }
}
