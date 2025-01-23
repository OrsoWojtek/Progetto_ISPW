package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.fx.handler.shortcut.CompleteShortcutHandlerFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (1/3)]----
public class QuizFX extends CompleteShortcutHandlerFX {
    @FXML
    private Label titoloQuiz; //Titolo del quiz
    @FXML
    private Label durataQuiz; //Durata del quiz
    @FXML
    private Label domandeQuiz; //Numero di domande del quiz
    @FXML
    private Label difficoltaQuiz; //Difficoltà del quiz
    private QuizInfoBean quiz; //Riferimento al quiz della pagina
    private QuizController quizController; //Riferimento al controller applicativo associato
    @FXML
    public void initialize(){
        quizController = new QuizController();
        try {
            quiz = quizController.getInfoQuiz();
            titoloQuiz.setText(quiz.getTitolo());
            durataQuiz.setText(quiz.getDurata()+" minuti");
            domandeQuiz.setText(quiz.getNumeroDomandeBean()+"");
            difficoltaQuiz.setText(quiz.getDifficolta());
        } catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            onLogoutButtonClicked();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError("Si è presentato un errore nel casting di un qualche dato conservato nella sessione.",ErrorCode.CASTING);
            onLogoutButtonClicked();
        }
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            quizController.clean();
            logout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
        }
    }
    //----METODO PER MOSTRARE GLI ARGOMENTI DEL QUIZ----
    @FXML
    public void onArgomentiClicked(){
        showMessageHandler.showMessage(quiz.getArgomenti(), "Argomenti del quiz");
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        quizController.clearOtherInfo();
        home();
    }
    //----METODO PER TORNARE ALLA PAGINA DEL CORSO----
    @FXML
    public void onTornaAlCorso(){
        quizController.clearInfoQuiz();
        course();
    }
    //----METODO AL CLICK DEL TASTO DI AVVIO DEL QUIZ----
    @FXML
    public void avviaQuiz(){
        try{
            pageLoader.loadPage(PageID.SVOLGIMENTO_QUIZ);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        }
    }
}
