package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.ErroriQuizInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.view.fx.handler.shortcut.CompleteShortcutHandlerFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (3/3)]----
public class EsitoQuizFX extends CompleteShortcutHandlerFX {
    @FXML
    private Label punteggio; //Punteggio ottenuto nel quiz
    @FXML
    private Label tempo; //Tempo impiegato a finire il quiz
    private QuizController controllerQuiz; //Riferimento al controller applicativo

    @FXML
    public void initialize() {
        controllerQuiz = new QuizController();
        try {
            QuizInfoBean quiz = controllerQuiz.getInfoQuiz();
            punteggio.setText(quiz.getPunteggioStudente()+"/"+ quiz.getPunteggio());
            tempo.setText(quiz.showTempo());
        }catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            effettuaLogout();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
            effettuaLogout();
        }
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void effettuaLogout(){
        try {
            controllerQuiz.clean();
            logout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
            System.exit(1);
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void tornaAllaHome(){
        controllerQuiz.clearOtherInfo();
        home();
    }
    //----METODO PER TORNARE ALLA PAGINA DEL CORSO----
    @FXML
    public void onTornaAlCorsoClicked(){
        controllerQuiz.clearInfoQuiz();
        course();
    }
    //----METODO PER MOSTRARE GLI ERRORI COMPIUTI----
    @FXML
    public void onErroriButtonClicked(){
        try {
            ErroriQuizInfoBean errori = controllerQuiz.getErrori();
            showMessageHandler.showMessage(errori.toString(),"Errori");
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
        }
    }
}
