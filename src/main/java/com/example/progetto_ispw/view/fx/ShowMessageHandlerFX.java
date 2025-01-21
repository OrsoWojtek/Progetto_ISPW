package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.view.ShowMessageHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

//----CLASSE PER GESTIRE I MESSAGGI DI ERRORE DA MOSTRARE A SCHERMO (VIEW: JAVAFX)
public class ShowMessageHandlerFX implements ShowMessageHandler {
    @Override
    public void showError(String message, String title){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
    @Override
    public void showMessage(String message, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
    @Override
    public boolean askConfirmation(String header, String content){
        //Mostra una finestra di dialogo di conferma
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait(); //Mostra il dialogo e aspetta la risposta dell'utente
        return result.isPresent() && result.get() == ButtonType.OK; //Se l'utente ha premuto "OK" (conferma) ritorna 'true', mentre se ha premuto "Annulla" o ha chiuso la finestra ritorna 'false'
    }
}
