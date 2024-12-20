package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.controller.CorsoController;
import com.example.progetto_ispw.controller.HomeController;
import com.example.progetto_ispw.model.Sessione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoFX {
    @FXML
    private Label nomeCorso; //Username dell'utente loggato
    @FXML
    public void initialize(){
        nomeCorso.setText(Sessione.getCourse().getNome()+"\n"+Sessione.getCourse().getDescrizione()); //Mostra nella home l'username dell'utente
    }

    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            CorsoController corsoController = new CorsoController();
            corsoController.clean();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml"))); //Torna alla pagina di login
            Stage stage = (Stage) nomeCorso.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            showErrorPopup("Errore nell'apertura della pagina di login","Pagina non trovata");
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        try {
            CorsoController corsoController = new CorsoController();
            corsoController.clearInfoCourse();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/home.fxml"))); //Torna alla pagina di home
            Stage stage = (Stage) nomeCorso.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            showErrorPopup("Errore nell'apertura della pagina di home","Pagina non trovata");
        }
    }
    //----METODO PER MOSTRARE MESSAGGI DI ERRORE----
    private void showErrorPopup(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
}
