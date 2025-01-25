package com.example.progetto_ispw.view.cli;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.PageManager;

import java.util.Scanner;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: LOGIN)----
public class LoginCLI extends PageManager {
    private final Scanner scanner = new Scanner(System.in);
    private static final String DIVISORE = "=================================";

    //Metodo principale per avviare il flusso di login
    @Override
    public void initialize() {
        while (true) {
            System.out.println(DIVISORE);
            System.out.println("🔑 BENVENUTO");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("3. Esci");
            System.out.println(DIVISORE);
            System.out.print("Seleziona un'opzione (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": //Login
                    handleLogin();
                    break;

                case "2": //Registrazione
                    onRegistrazione();
                    break;

                case "3": //Uscita
                    System.out.println(DIVISORE);
                    System.out.println("👋 Grazie per aver utilizzato l'applicazione. Arrivederci!");
                    System.out.println(DIVISORE);
                    return;

                default:
                    System.out.println(DIVISORE);
                    System.out.println("❌ Opzione non valida. Riprova!");
                    System.out.println(DIVISORE);
                    break;
            }
        }
    }

    //Gestione del login
    private void handleLogin() {
        System.out.println(DIVISORE);
        System.out.println("🔑 LOGIN");
        System.out.println(DIVISORE);

        //Richiedi all'utente di inserire username e password
        System.out.print("Inserisci il tuo username: ");
        String usernameInsert = scanner.nextLine().trim();

        System.out.print("Inserisci la tua password: ");
        String passwordInsert = scanner.nextLine().trim();

        //Controlla se i campi sono vuoti
        if (usernameInsert.isEmpty() || passwordInsert.isEmpty()) {
            showMessageHandler.showError(StandardMessagge.CREDENTIAL.getValue(), ErrorCode.CREDENTIAL_NOT_FOUND);
        } else {
            onLogin(usernameInsert, passwordInsert);
        }
    }

    //Metodo chiamato al click del pulsante di login
    public synchronized void onLogin(String username, String password) {
        LoginInfoBean bean = new LoginInfoBean();               //Istanziamento bean per il login
        LoginController controller = new LoginController();     //Istanziamento controller
        bean.setUsername(username);                             //Setting del bean
        bean.setPassword(password);

        try {
            controller.checkLogin(bean);     //Se le credenziali inserite sono corrette...
            System.out.println(DIVISORE);
            System.out.println("✅ Login effettuato con successo!");
            System.out.println(DIVISORE);
            pageLoader.loadPage(PageID.HOME);    //...Carica la pagina di home
        } catch (DataNotFoundException e) {      //Altrimenti...
            showMessageHandler.showError(e.getMessage(), ErrorCode.CREDENTIAL_ERROR);   //...Mostra un errore
        } catch (DataAccessException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.DB_ERROR);
        } catch (RoleNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.UNDEF_ROLE);
        } catch (PageNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.PAGE_NOT_FOUND);
        } catch (ConnectionException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
        }
    }

    //Metodo chiamato quando viene richiesta la registrazione
    public void onRegistrazione() {
        System.out.println(DIVISORE);
        System.out.println("📝 REGISTRAZIONE");
        System.out.println(DIVISORE);
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        System.out.println(DIVISORE);
    }
}
