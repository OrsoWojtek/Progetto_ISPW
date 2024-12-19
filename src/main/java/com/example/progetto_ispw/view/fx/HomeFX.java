package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.controller.HomeController;
import com.example.progetto_ispw.model.Sessione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: HOME)----
public class HomeFX {
    @FXML
    private Label username; //Username dell'utente loggato
    @FXML
    private ImageView avatar; //Avatar dell'utente loggato
    private List<String> courseNames;
    @FXML
    public void initialize(){
        HomeController home = new HomeController();
        String currentUser = Sessione.getUser().getUsername();
        username.setText(username.getText()+" "+currentUser); //Mostra nella home l'username dell'utente
        courseNames = home.getCorsiFrequentati();
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/"+Sessione.getUser().getRole().toLowerCase()+"_avatar.png")).toExternalForm()));
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            Sessione.clear(); //Cancello le informazioni riguardanti la sessione
            Connessione conn = Connessione.getInstance();
            conn.closeConnection(); //Chiudo definitivamente la connessione con il db
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml"))); //Torna alla pagina di login
            Stage stage = (Stage) username.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            showErrorPopup("Errore nell'apertura della pagina di login","Pagina non trovata");
        }
    }
    //----METODO PER APRIRE LA BARRA DI RICERCA PER ISCRIVERSI AI CORSI----
    @FXML
    public void onSearchButtonClicked() {
        showErrorPopup("L'iscrizione ad un nuovo corso è al momento disabilitata.\nCi dispiace per il disagio.","Funzionalità inesistente");
    }

    private void showErrorPopup(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }

}
