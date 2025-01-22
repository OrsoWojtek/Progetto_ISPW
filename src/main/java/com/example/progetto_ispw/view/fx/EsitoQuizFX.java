package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (3/3)]----
public class EsitoQuizFX extends PageManager {
    @FXML
    private Label punteggio; //Punteggio ottenuto nel quiz
    @FXML
    private Label tempo; //Tempo impiegato a finire il quiz
    private QuizController controllerQuiz; //Riferimento al controller applicativo

    @FXML
    public void initialize() {
        controllerQuiz = new QuizController();
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void effettuaLogout(){
        try {
            controllerQuiz.clean();
            pageLoader.loadPage(PageID.LOGIN);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.PAGE_NOT_FOUND);
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void tornaAllaHome(){
        controllerQuiz.clearOtherInfo();
        try{
            pageLoader.loadPage(PageID.HOME);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        }
    }
    //----METODO PER TORNARE ALLA PAGINA DEL CORSO----
    @FXML
    public void onTornaAlCorsoClicked(){
        controllerQuiz.clearInfoQuiz();
        try{
            pageLoader.loadPage(PageID.CORSO);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        }
    }
}
