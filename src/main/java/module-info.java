module com.example.progetto_ispw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.progetto_ispw to javafx.fxml;
    exports com.example.progetto_ispw;
    exports com.example.progetto_ispw.exception;
    exports com.example.progetto_ispw.view.fx;
    opens com.example.progetto_ispw.view.fx to javafx.fxml;
    exports com.example.progetto_ispw.controller;
    opens com.example.progetto_ispw.controller to javafx.fxml;
    exports com.example.progetto_ispw.bean;
    opens com.example.progetto_ispw.bean to javafx.fxml;
    exports com.example.progetto_ispw.dao.jdbc;
    opens com.example.progetto_ispw.dao.jdbc to javafx.fxml;
}