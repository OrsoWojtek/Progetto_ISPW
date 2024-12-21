package com.example.progetto_ispw.view.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
//----CLASSE UTILITY PER ESEGUIRE LE OPERAZIONI RELATIVE ALLE SHORTCUT
public class PageLoaderFX {
    //----COSTRUTTORE PRIVATO PER IMPEDIRE L'ISTANZA----
    private PageLoaderFX(){}
    //----METODO PER EFFETTUARE IL CAMBIO PAGINA----
    public static void loadPage(Label label, String page){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(PageLoaderFX.class.getResource("/"+page.toLowerCase()+".fxml"))); //Carica la pagina richiesta
            Stage stage = (Stage) label.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            showErrorPopup("Errore nell'apertura della pagina di "+page.toLowerCase(),"Pagina non trovata");
        }
    }
    //----METODO PER MOSTRARE MESSAGGI DI ERRORE----
    public static void showErrorPopup(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
}
