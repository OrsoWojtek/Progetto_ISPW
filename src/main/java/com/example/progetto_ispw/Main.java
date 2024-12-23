package com.example.progetto_ispw;

import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.fx.PageLoaderFX;
import com.example.progetto_ispw.view.fx.ShowErrorHandlerFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        PageManager loginPage;

        //Versione FX: ----INIZIO----
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/login.fxml"));
        Parent root = fxmlLoader.load();
        loginPage = fxmlLoader.getController();
        loginPage.setupDependencies(new PageLoaderFX(),new ShowErrorHandlerFX());
        Scene scene = new Scene(root);
        stage.setTitle("ILearn");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        //-----------------FINE------
    }

    public static void main(String[] args) {
        launch();
    }
}