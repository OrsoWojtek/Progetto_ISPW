package com.example.progetto_ispw;

import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.fx.handler.pageloader.PageLoaderFX;
import com.example.progetto_ispw.view.fx.handler.showmessage.ShowMessageHandlerFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*
----Devo ancora creare astrazione per i dao in generale, e applicarla nei controller;
----Devo faro i dao in filesystem;
----Devo implementare la cli;
----Devo mettere il meccanismo di scelta tra i vari dao e tra le view
----Devo fare le classi di test (3 minimo);
int i = 0;
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        PageManager loginPage;

        //Versione FX: ----INIZIO----
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/login.fxml"));
        Parent root = fxmlLoader.load();
        loginPage = fxmlLoader.getController();
        loginPage.setupDependencies(new PageLoaderFX(),new ShowMessageHandlerFX());
        Scene scene = new Scene(root);
        stage.setTitle("ILearn");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        //-----------------FINE------
    }
    public static void main(String[] args) {launch();}
}