package com.example.progetto_ispw.view.cli;
import com.example.progetto_ispw.bean.ErroriQuizInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.view.cli.handler.OutputHandler;
import com.example.progetto_ispw.view.handler.shortcut.concrete.CompleteShortcutHandler;

import java.util.Scanner;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (3/3)]----
public class EsitoQuizCLI extends CompleteShortcutHandler {
    private QuizController controllerQuiz; // Riferimento al controller applicativo
    //----METODO PER L'INIZIALIZZAZIONE DELLA GRAFICA----
    @Override
    public void initialize() {
        controllerQuiz = new QuizController();
        try {
            QuizInfoBean quiz = controllerQuiz.getInfoQuiz();
            OutputHandler.showln("=== Esito Quiz ===");
            OutputHandler.showln("Punteggio: " + quiz.getPunteggioStudente() + "/" + quiz.getPunteggio());
            OutputHandler.showln("Tempo impiegato: " + quiz.showTempo());
            mostraMenu();
        }catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            effettuaLogout();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
            effettuaLogout();
        }
    }

    //----METODO PER MOSTRARE IL MENU PRINCIPALE----
    private void mostraMenu() {
        Scanner scanner = new Scanner(System.in);

        OutputHandler.showln("\n--- Menu Esito Quiz ---");
        OutputHandler.showln("[E] Visualizza errori");
        OutputHandler.showln("[H] Torna alla home");
        OutputHandler.showln("[C] Torna alla pagina del corso");
        OutputHandler.showln("[L] Effettua logout");

        OutputHandler.show("Scelta: ");
        String scelta = scanner.nextLine().toUpperCase();

        switch (scelta) {
            case "E":
                mostraErrori();
                mostraMenu();
                break;
            case "H":
                tornaAllaHome();
                return; //Torna al menu precedente o termina l'app
            case "C":
                tornaAlCorso();
                return; //Torna al menu precedente o termina l'app
            case "L":
                effettuaLogout();
                return; //Effettua il logout
            default:
                OutputHandler.showln("Opzione non valida. Riprova.");
                mostraMenu();
        }
    }

    //----METODO PER MOSTRARE GLI ERRORI DEL QUIZ----
    private void mostraErrori() {
        try {
            ErroriQuizInfoBean errori = controllerQuiz.getErrori();
            showMessageHandler.showMessage(errori.toString(),"Errori");
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
        }
    }

    //----METODO PER TORNARE ALLA HOME----
    private void tornaAllaHome() {
        controllerQuiz.clearOtherInfo();
        home();
    }

    //----METODO PER EFFETTUARE IL LOGOUT----
    private void effettuaLogout() {
        try {
            controllerQuiz.clean();
            logout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
            System.exit(1);
        }
    }

    //----METODO PER TORNARE ALLA PAGINA DEL CORSO----
    private void tornaAlCorso() {
        controllerQuiz.clearInfoQuiz();
        course();
    }
}
