package com.example.progetto_ispw.view.cli;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.cli.handler.OutputHandler;
import com.example.progetto_ispw.view.handler.shortcut.concrete.CompleteShortcutHandler;

import java.util.Scanner;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (1/3)]----
public class QuizCLI extends CompleteShortcutHandler {
    private QuizInfoBean quiz; //Riferimento al quiz della pagina
    private QuizController quizController; //Riferimento al controller applicativo associato
    private final Scanner scanner = new Scanner(System.in); //Scanner per input utente
    //----METODO PER L'INIZIALIZZAZIONE DELLA GRAFICA----
    @Override
    public void initialize() {
        quizController = new QuizController();
        try {
            quiz = quizController.getInfoQuiz();
            mostraInformazioniQuiz();
            mostraMenu();
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            onLogout();
        } catch (DataSessionCastingException e) {
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
            onLogout();
        }
    }

    //----METODO PER MOSTRARE LE INFORMAZIONI DEL QUIZ----
    private void mostraInformazioniQuiz() {
        OutputHandler.showln("Titolo del quiz: " + quiz.getTitolo());
        OutputHandler.showln("Durata del quiz: " + quiz.getDurata() + " minuti");
        OutputHandler.showln("Numero di domande: " + quiz.getNumeroDomandeBean());
        OutputHandler.showln("Difficoltà: " + quiz.getDifficolta());
    }

    //----METODO PER MOSTRARE IL MENU PRINCIPALE----
    private void mostraMenu() {
        OutputHandler.showln("\n--- Menu Quiz ---");
        OutputHandler.showln("[A] Mostra argomenti del quiz");
        OutputHandler.showln("[S] Avvia il quiz");
        OutputHandler.showln("[C] Torna al corso");
        OutputHandler.showln("[H] Torna alla home");
        OutputHandler.showln("[L] Logout");
        OutputHandler.show("Seleziona un'opzione: ");

        String scelta = scanner.nextLine().trim().toUpperCase();
        switch (scelta) {
            case "A":
                onArgomenti();
                mostraMenu();
                break;
            case "H":
                onHome();
                return; //Esce dal menu corrente
            case "C":
                onTornaAlCorso();
                return; //Esce dal menu corrente
            case "S":
                avviaIlQuiz();
                return; //Esce dal menu corrente per caricare il quiz
            case "L":
                onLogout();
                return; //Esce dal menu corrente
            default:
                OutputHandler.showln("Opzione non valida. Riprova.");
                mostraMenu();
        }
    }

    //----METODO PER MOSTRARE GLI ARGOMENTI DEL QUIZ----
    private void onArgomenti() {
        showMessageHandler.showMessage(quiz.getArgomenti(), "Argomenti del quiz");
    }

    //----METODO PER TORNARE ALLA HOME----
    private void onHome() {
        quizController.clearOtherInfo();
        home();
    }

    //----METODO PER TORNARE ALLA PAGINA DEL CORSO----
    private void onTornaAlCorso() {
        quizController.clearInfoQuiz();
        course();
    }

    //----METODO PER EFFETTUARE IL LOGOUT----
    private void onLogout() {
        try {
            quizController.clean();
            logout();
        } catch (ConnectionException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            System.exit(1);
        }
    }

    //----METODO PER AVVIARE IL QUIZ----
    private void avviaIlQuiz() {
        OutputHandler.showln("\nCaricando il quiz...");
        try {
            pageLoader.loadPage(PageID.SVOLGIMENTO_QUIZ);
        } catch (PageNotFoundException e) {
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        }
    }

}
