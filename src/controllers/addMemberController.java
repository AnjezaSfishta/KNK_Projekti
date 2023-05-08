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
import repositories.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class addMemberController implements Initializable {

    DatabaseHandler handler;

    @FXML
    private Button cancel;

    @FXML
    private TextField email;

    @FXML
    private TextField id;

    @FXML
    private TextField phone;

    @FXML
    private TextField name;

    @FXML
    private Button save;

    @FXML
    private AnchorPane root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();

    }

    private Boolean isInEditMode = Boolean.FALSE;

    @FXML
    void addMember (ActionEvent event) {
        String memberName = name.getText();
        String memberID = id.getText();
        String memberPhone = phone.getText();
        String memberEmail = email.getText();


        if(isInEditMode){
            handleEditOperation();
            return;
        }

        if (memberName.isEmpty() || memberID.isEmpty() || memberPhone.isEmpty() || memberEmail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  Enter in all fields");
            alert.showAndWait();
            return;
        }

        String query = "INSERT INTO MEMBER VALUES (" +
                "'" + memberID + "'," +
                "'" + memberName + "'," +
                "'" + memberPhone + "'," +
                "'" + memberEmail + "'" +
                ")";
        System.out.println(query);
        if (handler.execAction(query)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Saved");
            alert.showAndWait();
            clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error Occured");
            alert.showAndWait();
            clear();
        }
    }

    private void handleEditOperation() {

        models.Member member = new models.Member(id.getText(),name.getText(),email.getText(),phone.getText());
        if(handler.updateMember(member)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("Success! Member updated");
            alert.showAndWait();
            clear();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Failed ! Member can not be updated");
            alert.showAndWait();
            clear();
        }
    }

    public void inflatedUI (models.Member member){
       // String gender = member.getGender();
        id.setText(member.getMemberID());
        name.setText(member.getName());
        email.setText(member.getEmail());
        phone.setText(member.getPhone());
        id.setEditable(false);
        isInEditMode = Boolean.TRUE;
//        female.setSelected(true);
//        if(gender.toLowerCase() == "male"){
//            male.setSelected(true);
//        }else if (gender.toLowerCase() == "female"){
//            female.setSelected(true);
//        }
    }

    @FXML
    void cancel (ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    void clear() {
        id.setText("");
        name.setText("");
        phone.setText("");
        email.setText("");
        //
        //
    }

}
