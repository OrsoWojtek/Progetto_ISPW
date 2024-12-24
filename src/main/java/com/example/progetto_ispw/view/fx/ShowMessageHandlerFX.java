package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.view.ShowMessageHandler;
import javafx.scene.control.Alert;
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
}
