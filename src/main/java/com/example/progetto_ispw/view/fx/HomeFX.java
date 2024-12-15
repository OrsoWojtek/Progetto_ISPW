package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.model.Sessione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: HOME)----
public class HomeFX {
    @FXML
    private Label username; //Username dell'utente loggato
    @FXML
    public void initialize(){
        String currentUser = Sessione.getUsername();
        username.setText(username.getText()+" "+currentUser); //Mostra nella home l'username dell'utente
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked() throws IOException, SQLException {
        Sessione.clear(); //Cancello le informazioni riguardanti la sessione
        Connessione conn = Connessione.getInstance();
        conn.closeConnection(); //Chiudo definitivamente la connessione con il db
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
        Stage stage = (Stage) username.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
