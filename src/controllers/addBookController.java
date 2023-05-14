package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Book;
import services.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private AnchorPane root;

    private Boolean isInEditMode = Boolean.FALSE;
    PieChart bookChart;
    PieChart memberChart;

    private void refreshGraphs(){
        bookChart.setData(databaseHandler.getBookGraphStatistics());
        memberChart.setData(databaseHandler.getMemberGraphStatistics());


    }

    @FXML
    void addBook(ActionEvent event) {
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();

        if(isInEditMode){
            handleEditOperation();
            return;
        }

        if (bookID.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter in all fields");
            alert.showAndWait();
            return;
        }


        // VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
        String query = "INSERT INTO BOOK VALUES ("+
                "'" + bookID + "'," +
                "'" + bookName + "'," +
                "'" + bookAuthor + "'," +
                "'" + bookPublisher + "'," +
                "" + "true" + "" +
                ")";
        System.out.println(query);

        if (databaseHandler.execAction(query)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            clear();
        } else {    //Nese nuk ka sukses atehere
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error");
            alert.showAndWait();
            clear();
        }

    }

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        databaseHandler = DatabaseHandler.getInstance();
        
        checkData();
        
    }

    private void checkData() {
        String query = "SELECT title FROM BOOK";
        ResultSet rs = databaseHandler.execQuery(query);
        try {
            while (rs.next()) {
                String titlex = rs.getString("title");
                System.out.println(titlex);
            }
        }catch(SQLException ex) {
            Logger.getLogger(addBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleEditOperation() {
        models.Book book = new models.Book(id.getText(),title.getText(),author.getText(),publisher.getText(), true);
        if(databaseHandler.updateBook(book)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("Success! Book updated");
            alert.showAndWait();
            clear();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Failed ! Book can not be updated");
            alert.showAndWait();
            clear();
        }
    }

    public void inflatedBUI (models.Book book){

        id.setText(book.getBookID());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        title.setText(book.getTitle());

        isInEditMode = Boolean.TRUE;

        id.setEditable(false);
    }


    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    void clear() {
        id.setText("");
        title.setText("");
        author.setText("");
        publisher.setText("");
//        quantity.setText("");
//        check.setSelected(false);
    }

}
