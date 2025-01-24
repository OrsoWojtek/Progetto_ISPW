package com.example.progetto_ispw.view.fx.handler.pageloader;

import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.handler.pageloader.PageLoader;
import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.fx.handler.showmessage.ShowMessageHandlerFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//----CLASSE PER ESEGUIRE LE OPERAZIONI RELATIVE AL CARICAMENTO DELLE PAGINE FXML
public class PageLoaderFX implements PageLoader {

    //----METODO PER EFFETTUARE IL CAMBIO PAGINA----
    @Override
    public void loadPage(PageID pageID) throws PageNotFoundException {
        String page = pageID.getValue();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/"+page.toLowerCase()+".fxml"));
            Parent root =loader.load(); //Carica la pagina richiesta
            Stage stage = (Stage) Stage.getWindows().getFirst();

            //Ottieni il controller collegato e lo inizializzi
            PageManager controller = loader.getController();
            controller.setupDependencies(new PageLoaderFX(),new ShowMessageHandlerFX());

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            throw new PageNotFoundException("Errore nell'apertura della pagina di "+page.toLowerCase());
        }
    }
}
