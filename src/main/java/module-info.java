module com.example.progetto_ispw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.progetto_ispw to javafx.fxml;
    exports com.example.progetto_ispw;
    exports com.example.progetto_ispw.exception;
    exports com.example.progetto_ispw.model;
    exports com.example.progetto_ispw.view;
    exports com.example.progetto_ispw.view.fx;
    opens com.example.progetto_ispw.view.fx to javafx.fxml;
    exports com.example.progetto_ispw.controller;
    opens com.example.progetto_ispw.controller to javafx.fxml;
    exports com.example.progetto_ispw.bean;
    opens com.example.progetto_ispw.bean to javafx.fxml;
    exports com.example.progetto_ispw.dao.jdbc;
    opens com.example.progetto_ispw.dao.jdbc to javafx.fxml;
    exports com.example.progetto_ispw.constants;
    exports com.example.progetto_ispw.view.fx.handler.showmessage;
    opens com.example.progetto_ispw.view.fx.handler.showmessage to javafx.fxml;
    exports com.example.progetto_ispw.view.fx.handler.pageloader;
    opens com.example.progetto_ispw.view.fx.handler.pageloader to javafx.fxml;
    exports com.example.progetto_ispw.view.fx.handler.shortcut;
    opens com.example.progetto_ispw.view.fx.handler.shortcut to javafx.fxml;
    exports com.example.progetto_ispw.view.handler.pageloader;
    exports com.example.progetto_ispw.view.handler.shortcut;
    exports com.example.progetto_ispw.view.handler.showmessage;
}