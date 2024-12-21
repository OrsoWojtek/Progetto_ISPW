package com.example.progetto_ispw;

import com.example.progetto_ispw.view.PageLoader;
import com.example.progetto_ispw.view.PageManagerAware;
import com.example.progetto_ispw.view.fx.PageLoaderFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        PageLoader pageLoader;
        PageManagerAware loginPage;
        //Versione FX: ----INIZIO----
        pageLoader= new PageLoaderFX(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/login.fxml"));
        Parent root = fxmlLoader.load();
        loginPage = fxmlLoader.getController();
        loginPage.setPageManager(pageLoader);
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