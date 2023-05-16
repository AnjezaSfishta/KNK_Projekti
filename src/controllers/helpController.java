package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class helpController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Label text;

    private ResourceBundle bundle;
    private Locale locale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize code here
    }

    @FXML
    private void btnEN(ActionEvent event) {
        loadLang("en_US");
    }

    @FXML
    private void btnAL(ActionEvent event) {
        loadLang("sq_AL");
    }

    private void loadLang(String lang) {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("Properties/button", locale);
        label.setText(bundle.getString("label"));
        text.setText(bundle.getString("text"));
    }
}
