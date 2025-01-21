package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (3/3)]----
public class EsitoQuizFX extends PageManager {
    private QuizController controllerQuiz; //Riferimento al controller applicativo

    @FXML
    public void initialize() {
        controllerQuiz = new QuizController();
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            controllerQuiz.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),"Errore connessione");
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        controllerQuiz.clearOtherInfo();
        try{
            pageLoader.loadPage("home");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        }
    }
    //----METODO PER TORNARE ALLA PAGINA DEL CORSO----
    @FXML
    public void onTornaAlCorsoClicked(){
        controllerQuiz.clearInfoQuiz();
        try{
            pageLoader.loadPage("corso");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        }
    }
}
