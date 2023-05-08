package controllers;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Book;
import repositories.DatabaseHandler;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class listBookController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, Boolean> availabilityCol;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private AnchorPane rootPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    @FXML
    private void handleBookDeleteOption(ActionEvent event) {
       Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
       if (selectedForDeletion == null) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error");
           alert.setHeaderText(null);
           alert.setContentText("Please select a book for deletion!");
           alert.showAndWait();
           return;
       }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Book");
        alert.setContentText("Are you sure you want to delete " + selectedForDeletion.getTitle()+"?");
        Optional<ButtonType> answer = alert.showAndWait();

        if(answer.get() == ButtonType.OK){
            //Delete Book
            Boolean result = DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
            if(result){
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText("SUCCESS");
                alert2.setContentText("Book Deleted!");
                alert2.showAndWait();
                list.remove(selectedForDeletion);
            }else{
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setHeaderText("Error");
                alert3.setContentText("Book could not be Deleted!");
                alert3.showAndWait();
            }
        }else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText("ERROR");
            alert1.setContentText("Deletion Cancelled");
            alert1.showAndWait();
        }


    }



    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }


    private void loadData() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
            String query = "SELECT * FROM BOOK";
            ResultSet rs = handler.execQuery(query);
            try {
                while (rs.next()) {
                    String title = rs.getString("title");
                    String id = rs.getString("bookId");
                    String author = rs.getString("author");
                    String publisher = rs.getString("publisher");
                    Boolean avail = rs.getBoolean("isAvail");

                    list.add(new Book(id, title, author, publisher, avail));
                }
            }catch(SQLException ex) {
                Logger.getLogger(listBookController.class.getName()).log(Level.SEVERE, null, ex);
            }

            tableView.setItems(list);
        }


    @FXML
    void editBookOption(ActionEvent event) {
        Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("No book selected ! Please select a book for edit-ing.");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addBook.fxml"));

            Parent parent = loader.load();

            addBookController controller = (addBookController) loader.getController();
            controller.inflatedBUI(selectedForEdit);
            Stage stage = new  Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex){
            Logger.getLogger(listBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
