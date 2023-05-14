package controllers;

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
        import services.DatabaseHandler;
        import models.Member;

        import java.io.IOException;
        import java.net.URL;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.Optional;
        import java.util.ResourceBundle;
        import java.util.logging.Level;
        import java.util.logging.Logger;

public class listMemberController implements Initializable {

    ObservableList<Member> list = FXCollections.observableArrayList();

    //@FXML
    //private TableColumn<Member> GenderCol;

    @FXML
    private TableColumn<Member, String> emailCol;

    @FXML
    private TableColumn<Member, String> idCol;

    @FXML
    private TableColumn<Member, String> nameCol;

    @FXML
    private TableColumn<Member, String> phoneCol;

    @FXML
    private TableView<Member> tableView;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadData() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String query = "SELECT * FROM MEMBER";
        ResultSet rs = handler.execQuery(query);
        try {
            while (rs.next()) {
                String id = rs.getString("memberId");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                list.add(new Member(id, name, phone, email));
            }
        }catch(SQLException ex) {
            Logger.getLogger(listMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);    }

    @FXML
    void deleteMemberOption(ActionEvent event) {
        Member selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            //AlertMaker.showErrorMessage("No member selected", "Please select a member for deletion.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("No member selected ! Please select a member for deletion.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Member");
        alert.setContentText("Are you sure you want to delete" + selectedForDeletion.getName()+"?");
        Optional<ButtonType> answer = alert.showAndWait();

        if(answer.get() == ButtonType.OK){
            //Delete Member
            Boolean result = DatabaseHandler.getInstance().deleteMember(selectedForDeletion);
            if(result){
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText("SUCCESS");
                alert2.setContentText("Member Deleted!");
                alert2.showAndWait();
                list.remove(selectedForDeletion);
            }else{
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setHeaderText("Error");
                alert3.setContentText("Member could not be Deleted!");
                alert3.showAndWait();
            }
        }else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText("ERROR");
            alert1.setContentText("Deletion Cancelled");
            alert1.showAndWait();
        }
    }

    @FXML
    void editMemberOption(ActionEvent event) {
        Member selectedForEdit = tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit == null) {
            //AlertMaker.showErrorMessage("No member selected", "Please select a member for deletion.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("No member selected ! Please select a member for edit-ing.");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/addMember.fxml"));

            Parent parent = loader.load();

            addMemberController controller = (addMemberController) loader.getController();
            controller.inflatedUI(selectedForEdit);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex){
            Logger.getLogger(listMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
