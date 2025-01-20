package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: QUIZ)----
public class QuizFX extends PageManager {
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
    private static final String PAGENOTFOUND = "Pagina non trovata";
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
            showMessageHandler.showError(e.getMessage(),"Errore di sessione");
            onLogoutButtonClicked();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError("Si è presentato un errore nel casting di un qualche dato conservato nella sessione.","Errore di casting");
            onLogoutButtonClicked();
        }
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            quizController.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),PAGENOTFOUND);
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),"Errore connessione");
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
        try{
            pageLoader.loadPage("home");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),PAGENOTFOUND);
        }
    }
    //----METODO PER TORNARE ALLA PAGINA DEL CORSO----
    @FXML
    public void onTornaAlCorso(){
        quizController.clearInfoQuiz();
        try{
            pageLoader.loadPage("corso");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),PAGENOTFOUND);
        }
    }
    //----METODO AL CLICK DEL TASTO DI AVVIO DEL QUIZ----
    @FXML
    public void avviaQuiz(){
        try{
            pageLoader.loadPage("svolgimento_quiz");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),PAGENOTFOUND);
        }
    }
}
