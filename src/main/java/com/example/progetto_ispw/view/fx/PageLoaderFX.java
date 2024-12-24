package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageLoader;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//----CLASSE PER ESEGUIRE LE OPERAZIONI RELATIVE AL CARICAMENTO DELLE PAGINE FXML
public class PageLoaderFX implements PageLoader {

    //----METODO PER EFFETTUARE IL CAMBIO PAGINA----
    @Override
    public void loadPage(String page) throws PageNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/"+page.toLowerCase()+".fxml"));
            Parent root =loader.load(); //Carica la pagina richiesta
            Stage stage = (Stage) Stage.getWindows().getFirst();
            // Ottieni il controller collegato
           Object controller = loader.getController();

            //Se il controller è una sottoclasse di PageManager, inizializzalo
            if (controller instanceof PageManager controllerFX) {
                controllerFX.setupDependencies(new PageLoaderFX(),new ShowMessageHandlerFX());
           }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            throw new PageNotFoundException("Errore nell'apertura della pagina di "+page.toLowerCase());
        }
    }
}
