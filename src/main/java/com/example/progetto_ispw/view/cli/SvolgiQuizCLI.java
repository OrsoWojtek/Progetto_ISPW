package com.example.progetto_ispw.view.cli;
import com.example.progetto_ispw.bean.AnsweringProcessInfoBean;
import com.example.progetto_ispw.bean.QuesitoInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.RispostaInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.cli.handler.OutputHandler;
import com.example.progetto_ispw.view.handler.shortcut.concrete.BaseShortcutHandler;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (2/3)]----
public class SvolgiQuizCLI extends BaseShortcutHandler {
    private int remainingTime; //Tempo rimanente (in secondi)
    private List<QuesitoInfoBean> quesiti; //Lista dei quesiti
    private int indxCurrentQuesito = 0; //Indice della domanda corrente
    private QuizController controllerApplicativo; //Controller applicativo
    private QuizInfoBean quiz; //Riferimento al quiz
    private final Scanner scanner = new Scanner(System.in); //Scanner per input utente
    private ScheduledExecutorService scheduler; // Scheduler per il timer

    //----METODO PER L'INIZIALIZZAZIONE DELLA GRAFICA----
    @Override
    public void initialize() {
        controllerApplicativo = new QuizController();
        try {
            quiz = controllerApplicativo.getInfoQuiz();

            String nameQuiz = quiz.getTitolo();
            quesiti = quiz.getQuesiti();
            remainingTime = quiz.getDurata() * 60;

            OutputHandler.showln("Benvenuto al quiz: " + nameQuiz);
            startTimer();
            showQuesito();

        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            logoutProcess();
        } catch (DataSessionCastingException e) {
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
            logoutProcess();
        }
    }

    //----METODO PER MOSTRARE UN QUESITO----
    private void showQuesito() {
        QuesitoInfoBean quesito = quesiti.get(indxCurrentQuesito);
        int number = indxCurrentQuesito + 1;

        OutputHandler.showln("\nDomanda " + number + "/" + quesiti.size());
        OutputHandler.showln(quesito.getDomanda());
        List<RispostaInfoBean> risposte = quesito.getRisposte();

        for (int i = 0; i < risposte.size(); i++) {
            OutputHandler.showln((i + 1) + ". " + risposte.get(i).getTesto());
        }

        OutputHandler.showln("Scegli una risposta (1-" + risposte.size() + ") o usa:");
        OutputHandler.showln("[P] Precedente, [N] Prossima domanda, [T] Termina quiz, [R] Tempo rimanente");

        handleInput(quesito);
    }

    //----METODO PER GESTIRE L'INPUT DELL'UTENTE----
    private void handleInput(QuesitoInfoBean quesito) {
        String input = scanner.nextLine();

        switch (input.toUpperCase()) {
            case "P": //Torna indietro
                if (indxCurrentQuesito > 0) {
                    indxCurrentQuesito--;
                    showQuesito();
                } else {
                    OutputHandler.showln("Non ci sono domande precedenti.");
                    showQuesito();
                }
                break;

            case "N": //Avanti
                if (indxCurrentQuesito < quesiti.size() - 1) {
                    indxCurrentQuesito++;
                    showQuesito();
                } else {
                    OutputHandler.showln("Non ci sono altre domande.");
                    showQuesito();
                }
                break;

            case "T": //Termina il quiz
                onTerminaEvent();
                break;

            case "R": //Mostra il tempo rimanente
                OutputHandler.showln("Tempo rimanente: " + formatTime(remainingTime));
                showQuesito();
                break;

            default: //Gestione della risposta
                try {
                    int rispostaIndex = Integer.parseInt(input) - 1;
                    if (rispostaIndex >= 0 && rispostaIndex < quesito.getRisposte().size()) {
                        AnsweringProcessInfoBean answer = new AnsweringProcessInfoBean();
                        answer.setQuesitoInfoBean(quesito);
                        answer.setRisposta(quesito.getRisposte().get(rispostaIndex).getTesto());
                        controllerApplicativo.tickAnswer(answer);
                        OutputHandler.showln("Risposta selezionata: " + quesito.getRisposte().get(rispostaIndex).getTesto());
                    } else {
                        OutputHandler.showln("Scelta non valida. Riprova.");
                    }
                } catch (NumberFormatException e) {
                    OutputHandler.showln("Input non valido. Inserisci un numero o un comando valido.");
                }
                showQuesito();
                break;
        }
    }

    //----METODO PER INIZIARE IL TIMER----
    private void startTimer() {
        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            if (remainingTime > 0) {
                remainingTime--;
            } else {
                scheduler.shutdown(); //Ferma lo scheduler
                termina();
            }
        }, 1, 1, TimeUnit.SECONDS); //Delay iniziale di 1 secondo, ripetizione ogni secondo
    }

    //----METODO PER FORMATTARE IL TEMPO IN MM:SS----
    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    //----METODO PER TERMINARE IL QUIZ----
    private void onTerminaEvent() {
        String header = "Vuoi davvero terminare il quiz?";
        String content = "";
        //Verifica se il quiz presenta dei quesiti senza risposta selezionata e, in caso di riscontro, chiede all'utente se è sicuro di voler terminare
        try{
            controllerApplicativo.isFullyFilled(quiz);
        } catch (NotFilledQuestionException e) {
            content = "Qualche quesito non presenta alcuna risposta selezionata."; //Aggiunta nella comunicazione che qualche quesito è senza risposta
        }
        content += "\nSe termini il quiz non potrai tornare indietro";
        if(showMessageHandler.askConfirmation(header,content)){
            termina();
        } else {
            showQuesito();
        }
    }

    //----METODO PER CHIUDERE IL QUIZ----
    private synchronized void termina() {
        if (scheduler != null && !scheduler.isShutdown()) { //Interrompiamo il timer se non è stato già fatto
            scheduler.shutdown();
        }
        try {
            quiz.setTempo(remainingTime);
            controllerApplicativo.submitQuiz(quiz);
            pageLoader.loadPage(PageID.ESITO_QUIZ);
        }catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            logoutProcess();
        }catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
            logoutProcess();
        }catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
            logoutProcess();
        }catch (DataAccessException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.DB_ERROR);
            logoutProcess();
        }catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            logoutProcess();
        }
    }

    //----METODO PER IL LOGOUT----
    private void logoutProcess() {
        try {
            controllerApplicativo.clean();
            logout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
            System.exit(1);
        }
    }
}
