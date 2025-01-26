package com.example.progetto_ispw;

import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.cli.LoginCLI;
import com.example.progetto_ispw.view.cli.handler.OutputHandler;
import com.example.progetto_ispw.view.cli.handler.pageloader.PageLoaderCLI;
import com.example.progetto_ispw.view.cli.handler.showmessage.ShowMessageHandlerCLI;
import com.example.progetto_ispw.view.fx.handler.pageloader.PageLoaderFX;
import com.example.progetto_ispw.view.fx.handler.showmessage.ShowMessageHandlerFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

/*
----Devo fare le classi di test (3 minimo);
int i = 0;
 */

public class Main extends Application {
    private final Scanner scanner = new Scanner(System.in); //Scanner per input utente

    @Override
    public void start(Stage stage) throws IOException {
        boolean continueRunning = true;

        while(continueRunning) {
            OutputHandler.showln("Come si vuole avviare il programma?");
            OutputHandler.showln("1. FX");
            OutputHandler.showln("2. CLI");
            OutputHandler.show("Seleziona un'opzione (1-2): ");
            String choice = scanner.nextLine().trim();

            PageManager loginPage; //Base per i controller grafici
            switch (choice) {
                case "1":
                    //Versione FX: ----INIZIO----
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/login.fxml"));
                    Parent root = fxmlLoader.load();
                    loginPage = fxmlLoader.getController();
                    loginPage.setupDependencies(new PageLoaderFX(), new ShowMessageHandlerFX());
                    Scene scene = new Scene(root);
                    stage.setTitle("ILearn");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                    //-----------------FINE------
                    continueRunning = false; //Esci dal loop dopo aver mostrato la UI
                    break;

                case "2":
                    //Versione CLI: ----INIZIO----
                    loginPage = new LoginCLI();
                    loginPage.setupDependencies(new PageLoaderCLI(), new ShowMessageHandlerCLI());
                    loginPage.initialize();
                    //-----------------FINE------
                    continueRunning = false; //Esci dal loop dopo aver mostrato la UI
                    break;
                default:
                    OutputHandler.showln("Opzione non valida. Riprova.");
                    break;
            }
        }
    }
    public static void main(String[] args) {
            launch();
    }
}