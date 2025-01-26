package com.example.progetto_ispw.view.cli;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.NotificheInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.constants.UserRole;
import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.cli.handler.OutputHandler;
import com.example.progetto_ispw.view.handler.shortcut.concrete.BaseShortcutHandler;

import java.util.*;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoCLI extends BaseShortcutHandler {
    private CorsoInfoBean corso; //Riferimento al corso della pagina
    private List<QuizInfoBean> quizzes; //Catalogo dei quiz disponibili
    private CorsoPageController controller; //Riferimento al controller applicativo associato
    private int currentPage = 0; //Indice della pagina corrente dei quiz da mostrare
    private UtenteInfoBean user; //Riferimento all'utente loggato
    private static final String NOTVALID = "Scelta non valida. Riprova.";
    private final Scanner scanner = new Scanner(System.in); //Scanner per input utente
    //----METODO PER L'INIZIALIZZAZIONE DELLA GRAFICA----
    @Override
    public void initialize(){
        controller = new CorsoPageController();
        try {
            user = controller.getInfoUser();
            corso = controller.getInfoCourse();
            quizzes = controller.getQuizDisponibili(corso, user);
            showQuizCatalog();
        } catch (DataSessionCastingException e) {
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
            onLogoutSelect();
        } catch (QuizCreationException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.QUIZ_NOT_FOUND);
            onLogoutSelect();
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
            onLogoutSelect();
        } catch (ConnectionException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            onLogoutSelect();
        } catch (DataAccessException e) {
            showMessageHandler.showError(e.getMessage(),ErrorCode.DB_ERROR);
            onLogoutSelect();
        }
    }
    //----METODO PER VISUALIZZARE IL CATALOGO DEI QUIZ----
    private void showQuizCatalog() {
        OutputHandler.showln("\n== " + corso.getNome() + " ==");

        int quizzesPerPage = 4;
        int startIndx = currentPage * quizzesPerPage;
        int endIndx = Math.min(startIndx + quizzesPerPage, quizzes.size());

        for (int i = startIndx; i < endIndx; i++) {
            QuizInfoBean quiz = quizzes.get(i);
            OutputHandler.showln(i+1 + ". " + quiz.getTitolo());

            if (user.getRole().equalsIgnoreCase(UserRole.STUDENTE.getValue())) {
                OutputHandler.showln("   Punteggio: "+quiz.getPunteggioStudente()+"/"+quiz.getPunteggio());
            } else {
                OutputHandler.showln("   [Modifica]");
            }
        }

        showNavigationOptions(endIndx);
    }
    //----METODO PER AGGIUNGERE OPZIONI DI NAVIGAZIONE----
    private void showNavigationOptions(int endIndx) {
        OutputHandler.showln("\nOpzioni:");
        if (currentPage > 0) {
            OutputHandler.showln("[P] Pagina precedente");
        }
        if (endIndx < quizzes.size()) {
            OutputHandler.showln("[N] Pagina successiva");
        }
        if ((!quizzes.isEmpty())&&(user.getRole().equalsIgnoreCase(UserRole.STUDENTE.getValue()))) {
            OutputHandler.showln("[S] Seleziona un quiz");
        }
        if (user.getRole().equalsIgnoreCase(UserRole.TUTOR.getValue())){
            OutputHandler.showln("[V] Visualizza notifiche");
            OutputHandler.showln("[A] Aggiungi quiz");
            OutputHandler.showln("[M] Modifica quiz");
        }
        if (user.getRole().equalsIgnoreCase(UserRole.STUDENTE.getValue())){
            OutputHandler.showln("[Q] Sollecita domanda");
        }
        OutputHandler.showln("[T] Consulta la teoria");
        OutputHandler.showln("[D] Descrizione del corso");
        OutputHandler.showln("[H] Torna alla home");
        OutputHandler.showln("[L] Logout");

        handleNavigationInput();
    }

    //----METODO PER GESTIRE L'INPUT DI NAVIGAZIONE----
    private void handleNavigationInput() {
        OutputHandler.show("Inserisci la tua scelta: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        //Mappa dei comandi e delle azioni
        Map<String, Runnable> actions = new HashMap<>();
        actions.put("P", this::previousPage);
        actions.put("N", this::nextPage);
        actions.put("D", this::onDescrizione);
        actions.put("L", this::onLogoutSelect);
        actions.put("H", this::onHomeSelect);
        actions.put("T", this::onTeorySelect);
        actions.put("M", () -> roleBasedAction(UserRole.STUDENTE, this::onModifica));
        actions.put("A", () -> roleBasedAction(UserRole.STUDENTE, this::onAddQuizSel));
        actions.put("V", () -> roleBasedAction(UserRole.STUDENTE, this::onSeeNotify));
        actions.put("Q", () -> roleBasedAction(UserRole.TUTOR, this::onQuestionReq));
        actions.put("S", () -> roleBasedAction(UserRole.TUTOR, this::selectQuiz));

        //Esegui l'azione corrispondente, o mostra un messaggio per scelta non valida
        actions.getOrDefault(choice, () -> {
            OutputHandler.showln(NOTVALID);
            showQuizCatalog();
        }).run();
    }
    //----METODO PER MOSTRARE LA PAGINA PRECEDENTE DEL CATALOGO DEI QUIZ----
    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
        } else {
            OutputHandler.showln("Non ci sono pagine precedenti.");
        }
        showQuizCatalog();
    }

    //----METODO PER MOSTRARE LA PAGINA SUCCESSIVA DEL CATALOGO DEI QUIZ----
    private void nextPage() {
        if ((currentPage + 1) * 6 < quizzes.size()) {
            currentPage++;
        } else {
            OutputHandler.showln("Non ci sono altre pagine.");
        }
        showQuizCatalog();
    }

    //----METODO PER VERIFICARE IL PERMESSO DI USARE UNA CERTA AZIONE IN BASE AL RUOLO DELL'UTENTE----
    private void roleBasedAction(UserRole forbiddenRole, Runnable action) {
        if (user.getRole().equalsIgnoreCase(forbiddenRole.getValue())) {
            OutputHandler.showln(NOTVALID);
            showQuizCatalog();
        } else {
            action.run();
        }
    }
    //----METODO PER GESTIRE LA RICHIESTA DI FARE UNA DOMANDA----
    public void onQuestionReq(){
        showMessageHandler.showError("Sollecita una domanda: "+StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showQuizCatalog();
    }
    //----METODO PER MOSTRARE LE ULTIME NOTIFICHE----
    public void onSeeNotify(){
        try {
            NotificheInfoBean notifiche = controller.getInfoNotifiche();
            showMessageHandler.showMessage(notifiche.getNotifiche(),"Notifiche");
            showQuizCatalog();
        } catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
            onLogoutSelect();
        } catch (DataAccessException e) {
            showMessageHandler.showError(e.getMessage(),ErrorCode.DB_ERROR);
            onLogoutSelect();
        }catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
            onLogoutSelect();
        }catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            onLogoutSelect();
        }
    }
    //----METODO PER MOSTRARE LA DESCRIZIONE DEL CORSO----
    public void onDescrizione(){
        showMessageHandler.showMessage(corso.getDescrizione(), "Descrizione del corso");
        showQuizCatalog();
    }
    //----METODO PER MOSTRARE LA PAGINA DI TEORIA DEL CORSO----
    public void onTeorySelect(){
        showMessageHandler.showError("Consulta teoria: "+StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showQuizCatalog();
    }
    //----METODO PER ANDARE ALLA PAGINA DI CREAZIONE DEI QUIZ----
    public void onAddQuizSel(){
        showMessageHandler.showError("Aggiungi un quiz: "+StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showQuizCatalog();
    }
    //----METODO PER ANDARE ALLA PAGINA DI MODIFICA DEI QUIZ----
    public void onModifica(){
        showMessageHandler.showError("Modifica un quiz"+StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showQuizCatalog();
    }
    //----METODO PER SELEZIONARE UN QUIZ----
    private void selectQuiz() {
        if(!quizzes.isEmpty()) {
            OutputHandler.show("Inserisci il numero del quiz da selezionare: ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (index >= 0 && index < quizzes.size()) {
                    goToQuizPage(quizzes.get(index).getTitolo());
                } else {
                    OutputHandler.showln("Indice non valido. Riprova.");
                    showQuizCatalog();
                }
            } catch (NumberFormatException e) {
                OutputHandler.showln("Inserisci un numero valido.");
                showQuizCatalog();
            }
        } else {
            OutputHandler.showln(NOTVALID);
            showQuizCatalog();
        }
    }
    //----METODO PER ANDARE ALLA PAGINA DEL QUIZ----
    private void goToQuizPage(String nomeQuiz) {
        Optional<QuizInfoBean> quizScelto = quizzes.stream().filter(quiz -> quiz.getTitolo().equals(nomeQuiz)).findFirst(); //Cerca il bean del quiz nel catalogo corrispondere al nome del quiz selezionato dall'utente
        quizScelto.ifPresentOrElse(currentQuiz -> {
            try {
                controller.setInfoQuiz(currentQuiz); //Passa il bean del quiz al controller applicativo
                pageLoader.loadPage(PageID.QUIZ); //...Mostra la pagina del quiz
            } catch (DataSessionCastingException e){
                showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
                onLogoutSelect();
            } catch (PageNotFoundException e){
                showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
                onLogoutSelect();
            } catch (DataNotFoundException e) {
                showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
                onLogoutSelect();
            }
        }, () -> {
            OutputHandler.showln("Quiz non trovato.");
            showQuizCatalog();
        });
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    private void onLogoutSelect() {
        try {
            controller.clean();
            logout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
            System.exit(1);
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    private void onHomeSelect() {
        controller.clearInfoCourse();
        home();
    }
}
