module Library.Management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    requires java.desktop;
    requires javafx.web;
    requires javafx.base;


    opens application;
    opens controllers;
    opens repositories; 
    
}
