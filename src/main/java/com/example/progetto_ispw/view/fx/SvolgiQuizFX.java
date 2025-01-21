package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.QuesitoInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.util.List;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (2/3)]----
public class SvolgiQuizFX extends PageManager {
    @FXML
    private Label nameQuiz; //Titolo del quiz
    @FXML
    private Label timer; //Durata del quiz
    @FXML
    private Label currentQuestion; //Domanda corrente del quiz (contatore)
    @FXML
    private Label domanda; //Domanda corrente del quiz (testo)
    @FXML
    private RadioButton risposta1; //Prima risposta selezionabile
    @FXML
    private RadioButton risposta2; //Seconda risposta selezionabile
    @FXML
    private RadioButton risposta3; //Terza risposta selezionabile
    @FXML
    private RadioButton risposta4; //Quarta risposta selezionabile
    private QuizController controllerApplicativo; //Riferimento al controller applicativo
    private QuizInfoBean quiz; //Riferimento al quiz della pagina
    private List<QuesitoInfoBean> quesiti; //Quesiti del quiz
    @FXML
    public void initialize(){
        controllerApplicativo = new QuizController();
        try{
            quiz = controllerApplicativo.getInfoQuiz();
            nameQuiz.setText(quiz.getTitolo());
            quesiti = quiz.getQuesiti();
            //metodo che fa vedere la durata iniziare e decrescere
            //metodo che sistema si occupa di mostrare i quesiti (ricorda la gestione dei tasti 'next' e 'back')
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(),"Errore di sessione");
            logout();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError("Si è presentato un errore nel casting di un qualche dato conservato nella sessione.","Errore di casting");
            logout();
        }
    }
    //----METODO PER LA CONFERMA DI CHIUSURA DEL QUIZ----
    @FXML
    public void onTerminaButtonClicked(){
        //VERIFICA SE IL QUIZ PRESENTA DEI QUESITI SENZA RISPOSTA SELEZIONATA E, IN CASO DI RISCONTRO, CHIEDE ALL'UTENTE SE È SICURO DI VOLER TERMINARE
    }
    //----METODO PER LA CHIUSURA DEL QUIZ----
    private void termina(){
        //CHIUDE IL QUIZ E APRE LA PAGINA DI ESITO
    }
    private void logout(){
        try {
            controllerApplicativo.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),"Errore connessione");
        }
    }
}
