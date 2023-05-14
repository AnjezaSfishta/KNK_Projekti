package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainController implements Initializable {
    public Tab bookIssueTab;
    DatabaseHandler databaseHandler;
    PieChart bookChart;
    PieChart memberChart;
    @FXML
    private TextField bookID;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookAuthor1;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField bookIDInput;

    @FXML
    private Text bookName;

    @FXML
    private VBox book_info;

    @FXML
    private Text bookStatus;

    @FXML
    private TextField memberIDInput;

    @FXML
    private Text memberName;

    @FXML
    private Text memberPhone;

    @FXML
    private ListView<String> issueDataList;

    @FXML
    Boolean isReadyForSubmission = false;
    @FXML
    private StackPane bookInfoContainer;
    @FXML
    private StackPane memberInfoContainer;


    public mainController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();
        initDrawer();
        initGraphs();
    }
    private void initGraphs() {
        bookChart = new PieChart(databaseHandler.getBookGraphStatistics());
        bookInfoContainer.getChildren().add(bookChart);
        memberChart = new PieChart(databaseHandler.getMemberGraphStatistics());
        memberInfoContainer.getChildren().add(memberChart);


    }

    private void initDrawer() {}
    private void refreshGraphs(){
        bookChart.setData(databaseHandler.getBookGraphStatistics());
        memberChart.setData(databaseHandler.getMemberGraphStatistics());


    }
    private void enableDisableGraphs(Boolean status){
        if(status){
            bookChart.setOpacity(1);
            memberChart.setOpacity(1);

        }else{
            bookChart.setOpacity(0);
            memberChart.setOpacity(0);
        }

    }



    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/View/addBook.fxml", "Add New Book");
        refreshGraphs();
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/View/addMember.fxml", "Add New Member");
        refreshGraphs();
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        loadWindow("/View/listBook.fxml", "Book List");
        refreshGraphs();
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        loadWindow("/View/listMember.fxml", "Member List");
        refreshGraphs();
    }

    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBook();
        String id = bookIDInput.getText();
        String query = "SELECT * FROM BOOK WHERE bookId = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(query);
        Boolean flag = false;

        try {
            while (rs.next()) {

                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");

                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                String status = (bStatus) ? "Available" : "Not Available";
                bookStatus.setText(status);

                flag = true;
            }
            if (!flag) {
                bookName.setText("No Such Book Available");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        clearMember();
        String id = memberIDInput.getText();
        String query = "SELECT * FROM MEMBER WHERE memberId = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(query);
        Boolean flag = false;

        try {
            while (rs.next()) {

                String mName = rs.getString("name");
                String mPhone = rs.getString("phone");

                memberName.setText(mName);
                memberPhone.setText(mPhone);

                flag = true;
            }
            if (!flag) {
                memberName.setText("No Such Member Available");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadIssueOperation (ActionEvent event) {
        String memberID = memberIDInput.getText();
        String bookID = bookIDInput.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to issue the book " + bookName.getText() + "\n to " + memberName.getText() + " ?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String str = "INSERT INTO ISSUE(bookId, memberID) VALUES("
                + "'" + bookID + "',"
                + "'" + memberID + "'" +
            ")";
            String str2 = "UPDATE BOOK SET isAvail = false WHERE bookId = '" + bookID + "'";
            System.out.println(str + " and " + str2);

            if (databaseHandler.execAction(str) && databaseHandler.execAction(str2)) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Success");
                alert2.setHeaderText(null);
                alert2.setContentText("Book Issue Complete");
                alert2.showAndWait();
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Failed");
                alert2.setHeaderText(null);
                alert2.setContentText("Issue Operation Failed");
                alert2.showAndWait();
            }
        } else {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Canceled");
                alert2.setHeaderText(null);
                alert2.setContentText("Issue Operation Cancelled");
                alert2.showAndWait();
            }
        clearIssueEntries();
    }

    @FXML
    private void loadBookInfo2(ActionEvent event) {

        ObservableList<String> issueData = FXCollections.observableArrayList();
        isReadyForSubmission = false;

        String id = bookID.getText();
        String query = "SELECT * FROM ISSUE WHERE bookId = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(query);

        try{
            while (rs.next()) {
                enableDisableGraphs(false);
                String mBookID= id;
                String mMemberID = rs.getString("memberId");
                Timestamp mIssueTime = rs.getTimestamp("issueTime");
                int mRenewCount = rs.getInt("renew_count");

                issueData.add("Issue Date and Time: " + mIssueTime.toGMTString());
                issueData.add("Renew Count: " + mRenewCount);

                issueData.add("Book Information: ");
                query = "SELECT * FROM BOOK WHERE bookId = '" + mBookID + "'";
                ResultSet rl = databaseHandler.execQuery(query);
                while (rl.next()) {
                    issueData.add("Book Name: " + rl.getString("title"));
                    issueData.add("Book ID: " + rl.getString("bookId"));
                    issueData.add("Book Author: " + rl.getString("author"));
                    issueData.add("Book Publisher: " + rl.getString("publisher"));
                }
                query = "SELECT * FROM MEMBER WHERE memberId = '" + mMemberID + "'";
                rl = databaseHandler.execQuery(query);
                issueData.add("Member Information: ");
                while (rl.next()) {
                    issueData.add("Member Name: " + rl.getString("name"));
                    issueData.add("Member Phone: " + rl.getString("phone"));
                    issueData.add("Member Email: " + rl.getString("email"));
                }

                isReadyForSubmission = true;
            }
        } catch(SQLException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        issueDataList.getItems().setAll(issueData);

    }

    void clearBook() {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
    }

    void clearMember() {
        memberName.setText("");
        memberPhone.setText("");
    }
    private void clearIssueEntries() {
        bookIDInput.clear();
        memberIDInput.clear();
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
        memberPhone.setText("");
        memberName.setText("");
        enableDisableGraphs(true);
    }

    @FXML
    public void loadSubmissionOp(ActionEvent actionEvent) {
        if (!isReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to submit");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to return the book? ");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String id = bookID.getText();
            String ac1 = "DELETE FROM ISSUE WHERE bookId = '" + id + "'";
            String ac2 = "UPDATE BOOK SET isAvail = TRUE WHERE bookId = '" + id + "'";

            if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book Has Been Submitted");
                alert.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText(null);
                alert.setContentText("Submitted Has Failed");
                alert.showAndWait();
            }
        }else {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Canceled");
            alert2.setHeaderText(null);
            alert2.setContentText("Submission Operation Cancelled");
            alert2.showAndWait();
        }
    }

    @FXML
    private void loadRenewOp (ActionEvent event) {
        if (!isReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to renew");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Renew Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to renew the book? ");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String ac = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count + 1 WHERE bookId = '" + bookID.getText() + "'";
            System.out.println(ac);
            if (databaseHandler.execAction(ac)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book Has Been Renewed");
                alert.showAndWait();
            }else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Failed");
                alert2.setHeaderText(null);
                alert2.setContentText("Renew Has Been Failed");
                alert2.showAndWait();
            }
        } else {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Canceled");
                alert2.setHeaderText(null);
                alert2.setContentText("Renew Operation Cancelled");
                alert2.showAndWait();
            }
    }

    public void handlerMenuClose(ActionEvent actionEvent) {
        ((Stage) rootPane.getScene().getWindow()).close();
        
    }

    public void handleMenuAddMember(ActionEvent actionEvent) {
        loadWindow("/View/addMember.fxml", "Add New Member");
    }

    public void handleMenuAddBook(ActionEvent actionEvent) {
        loadWindow("/View/addBook.fxml", "Add New Book");
    }

    public void handleMenuViewBook(ActionEvent actionEvent) {
        loadWindow("/View/listBook.fxml", "Book List");
    }

    public void handleMenuViewMember(ActionEvent actionEvent) {
        loadWindow("/View/listMember.fxml", "Member List");
    }
    public void handleHelp(ActionEvent actionEvent) {
        loadWindow("/View/help.fxml", "Help");
    }


    public void handleMenuFullScreen(ActionEvent actionEvent) {

        Stage stage = ((Stage) rootPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());


    }
}
