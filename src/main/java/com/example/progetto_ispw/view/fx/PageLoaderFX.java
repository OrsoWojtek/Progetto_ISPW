package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.view.PageLoader;
import com.example.progetto_ispw.view.PageManagerAware;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
//----CLASSE PER ESEGUIRE LE OPERAZIONI RELATIVE AL CARICAMENTO DELLE PAGINE FXML E ALLA GESTIONE DEI POPUP DI ERRORE
public class PageLoaderFX implements PageLoader {
    private Stage stage;

    //----COSTRUTTORE PRIVATO PER IMPEDIRE L'ISTANZA----
    public PageLoaderFX(Stage stage){
        this.stage = stage;
    }
    //----METODO PER EFFETTUARE IL CAMBIO PAGINA----
    @Override
    public void loadPage(String page){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/"+page.toLowerCase()+".fxml"));
            Parent root =loader.load(); //Carica la pagina richiesta
            // Ottieni il controller collegato
            Object controller = loader.getController();

            // Se il controller implementa un'interfaccia o un metodo setPageManager, imposta PageLoader
            if (controller instanceof PageManagerAware) {
                ((PageManagerAware) controller).setPageManager(this);
            }
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.show();
        } catch (IOException e){
            showErrorPopup("Errore nell'apertura della pagina di "+page.toLowerCase(),"Pagina non trovata");
        }
    }
    //----METODO PER MOSTRARE MESSAGGI DI ERRORE----
    @Override
    public void showErrorPopup(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
}
