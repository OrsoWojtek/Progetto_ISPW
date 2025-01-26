package com.example.progetto_ispw.view.cli;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.cli.handler.OutputHandler;

import java.util.Scanner;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: LOGIN)----
public class LoginCLI extends PageManager {
    private final Scanner scanner = new Scanner(System.in); //Scanner per input utente
    private static final String DIVISORE = "=================================";

    //----METODO PER L'INIZIALIZZAZIONE DELLA GRAFICA----
    @Override
    public void initialize() {
        OutputHandler.showln(DIVISORE);
        OutputHandler.showln("🔑 BENVENUTO");
        OutputHandler.showln("1. Login");
        OutputHandler.showln("2. Registrazione");
        OutputHandler.showln("3. Esci");
        OutputHandler.showln(DIVISORE);
        OutputHandler.show("Seleziona un'opzione (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": //Login
                handleLogin();
                break;

            case "2": //Registrazione
                onRegistrazione();
                break;

            case "3": //Uscita
                OutputHandler.showln(DIVISORE);
                OutputHandler.showln("👋 Grazie per aver utilizzato l'applicazione. Arrivederci!");
                OutputHandler.showln(DIVISORE);
                System.exit(0);
                break;

            default:
                OutputHandler.showln(DIVISORE);
                OutputHandler.showln("❌ Opzione non valida. Riprova!");
                OutputHandler.showln(DIVISORE);
                initialize();
                break;
        }
    }

    //----METODO PER LA GESTIONE DEL LOGIN----
    private void handleLogin() {
        OutputHandler.showln(DIVISORE);
        OutputHandler.showln("🔑 LOGIN");
        OutputHandler.showln(DIVISORE);

        //Richiedi all'utente di inserire username e password
        OutputHandler.show("Inserisci il tuo username: ");
        String usernameInsert = scanner.nextLine().trim();

        OutputHandler.show("Inserisci la tua password: ");
        String passwordInsert = scanner.nextLine().trim();

        //Controlla se i campi sono vuoti
        if (usernameInsert.isEmpty() || passwordInsert.isEmpty()) {
            showMessageHandler.showError(StandardMessagge.CREDENTIAL.getValue(), ErrorCode.CREDENTIAL_NOT_FOUND);
            initialize();
        } else {
            onLogin(usernameInsert, passwordInsert);
        }
    }

    //----METODO CHIAMATO ALLA RICHIESTA DI CORRETTA DI LOGIN----
    public synchronized void onLogin(String username, String password) {
        LoginInfoBean bean = new LoginInfoBean();               //Istanziamento bean per il login
        LoginController controller = new LoginController();     //Istanziamento controller
        bean.setUsername(username);                             //Setting del bean
        bean.setPassword(password);

        try {
            controller.checkLogin(bean);     //Se le credenziali inserite sono corrette...
            OutputHandler.showln(DIVISORE);
            OutputHandler.showln("✅ Login effettuato con successo!");
            OutputHandler.showln(DIVISORE);
            pageLoader.loadPage(PageID.HOME);    //...Carica la pagina di home
        } catch (PageNotFoundException e) {      //Altrimenti...
            showMessageHandler.showError(e.getMessage(), ErrorCode.PAGE_NOT_FOUND); //...Mostra un errore
            System.exit(1);
        } catch (DataAccessException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.DB_ERROR);
            initialize();
        } catch (ConnectionException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            System.exit(1);
        } catch (RoleNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.UNDEF_ROLE);
            initialize();
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CREDENTIAL_ERROR);
            initialize();
        }
    }

    //----METODO CHIAMATO QUANDO VIENE RICHIESTA LA REGISTRAZIONE----
    public void onRegistrazione() {
        OutputHandler.showln(DIVISORE);
        OutputHandler.showln("📝 REGISTRAZIONE");
        OutputHandler.showln(DIVISORE);
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        OutputHandler.showln(DIVISORE);
        initialize();
    }
}
