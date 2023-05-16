package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class helpController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Label text;
    @FXML
    private RadioButton radioEn;
    @FXML
    private RadioButton radioAl;

    private ResourceBundle bundle;
    private Locale locale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize code here
    }

    @FXML
    private void radioEn() {
        loadLang("en", "US");
    }

    @FXML
    private void radioAl() {
        loadLang("sq", "AL");
    }

    private void loadLang(String language, String country) {
        locale = new Locale(language, country);
        bundle = ResourceBundle.getBundle("Properties.button", locale);
        label.setText(bundle.getString("label"));
        text.setText(bundle.getString("text"));
    }
}
