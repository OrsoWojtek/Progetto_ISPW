package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.view.ShowErrorHandler;
import javafx.scene.control.Alert;
//----CLASSE PER GESTIRE I MESSAGGI DI ERRORE DA MOSTRARE A SCHERMO (VIEW: JAVAFX)
public class ShowErrorHandlerFX implements ShowErrorHandler {
    @Override
    public void showError(String message, String title){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
}
