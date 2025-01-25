package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.AnsweringProcessInfoBean;
import com.example.progetto_ispw.bean.QuesitoInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.handler.shortcut.concrete.BaseShortcutHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA [CASO SPECIFICO: QUIZ (2/3)]----
public class SvolgiQuizFX extends BaseShortcutHandler {
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
    @FXML
    private ImageView frecciaAvanti; //Tasto per mostrare il prossimo quesito
    @FXML
    private ImageView frecciaIndietro; //Tasto per mostrare il precedente quesito
    private QuizController controllerApplicativo; //Riferimento al controller applicativo
    private QuizInfoBean quiz; //Riferimento al quiz della pagina
    private List<QuesitoInfoBean> quesiti; //Quesiti del quiz
    private int indxCurrentQuesito = 0; //Indice di riferimento del quesito corrente
    private int remainingTime; //Tempo rimanente (in secondi)
    private Timeline timeline; //Timeline per l'animazione del timer
    @FXML
    public void initialize(){
        controllerApplicativo = new QuizController();
        try{
            quiz = controllerApplicativo.getInfoQuiz();
            nameQuiz.setText(quiz.getTitolo());
            quesiti = quiz.getQuesiti();
            remainingTime = quiz.getDurata()*60;
            startTimer();
            showQuesito(); //Metodo che si occupa di mostrare i quesiti (ricorda la gestione dei tasti 'next' e 'back')
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            logoutProcess();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
            logoutProcess();
        }
    }
    //----METODO PER INIZIALIZZARE IL POOL DI RISPOSTE----
    private void setUpPool(){
        //Pool di risposte
        ToggleGroup poolDiRisposte = risposta1.getToggleGroup();
        //Aggiungo un Listener
        poolDiRisposte.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            //Verifica quale RadioButton è stato selezionato
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                AnsweringProcessInfoBean answer = new AnsweringProcessInfoBean();
                answer.setQuesitoInfoBean(quesiti.get(indxCurrentQuesito));
                answer.setRisposta(selectedRadioButton.getText());
                controllerApplicativo.tickAnswer(answer);
            }

            //Verifica quale RadioButton era precedentemente selezionato
            if (oldValue != null) {
                RadioButton previousRadioButton = (RadioButton) oldValue;
                AnsweringProcessInfoBean answer = new AnsweringProcessInfoBean();
                answer.setQuesitoInfoBean(quesiti.get(indxCurrentQuesito));
                answer.setRisposta(previousRadioButton.getText());
                controllerApplicativo.untickAnswer(answer);
            }
        });
    }
    //----METODO PER MOSTRARE UN QUESITO DEL QUIZ----
    private void showQuesito(){
        QuesitoInfoBean quesito = quesiti.get(indxCurrentQuesito);
        int number = indxCurrentQuesito + 1; //Numero del quesito rispetto alla lista dei quesiti del quiz
        currentQuestion.setText("Domanda n."+number);
        domanda.setText(quesito.getDomanda());
        //Impostiamo testo e se è stata selezionata o meno ogni risposta
        risposta1.setText(quesito.getRisposte().getFirst().getTesto());
        risposta1.setSelected(quesito.getRisposte().getFirst().isTicked());
        risposta2.setText(quesito.getRisposte().get(1).getTesto());
        risposta2.setSelected(quesito.getRisposte().get(1).isTicked());
        risposta3.setText(quesito.getRisposte().get(2).getTesto());
        risposta3.setSelected(quesito.getRisposte().get(2).isTicked());
        risposta4.setText(quesito.getRisposte().get(3).getTesto());
        risposta4.setSelected(quesito.getRisposte().get(3).isTicked());
        setUpPool();
        showNavigationButtons();
    }
    //----METODO PER MOSTRARE I TASTI 'AVANTI' E 'INDIETRO'----
    private void showNavigationButtons(){
        //Se il quesito corrente non è il primo, mostra il tasto per tornare indietro
        if(indxCurrentQuesito > 0){
            frecciaIndietro.setVisible(true);
            frecciaIndietro.setOnMouseClicked(mouseEvent -> {
                indxCurrentQuesito--;
                showQuesito();
            });
        } else {
            //Altrimenti non mostrarlo
            frecciaIndietro.setVisible(false);
        }
        //Se ci sono ancora quesiti dopo quello corrente, mostra il tasto per andare avanti
        if(indxCurrentQuesito < (quesiti.size() - 1)){
            frecciaAvanti.setVisible(true);
            frecciaAvanti.setOnMouseClicked(mouseEvent -> {
                indxCurrentQuesito++;
                showQuesito();
            });
        } else {
            //Altrimenti non mostrarlo
            frecciaAvanti.setVisible(false);
        }
    }
    //----METODO PER INIZIARE L'ANIMAZIONE DEL TIMER----
    private void startTimer() {
        //Crea un'animazione che si ripete ogni secondo
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (remainingTime > 0) {
                remainingTime--;
                updateTimerLabel();
            } else {
                //Timer completato
                timeline.stop(); //Ferma l'animazione
                termina(); //Termina il quiz
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); //Ripeti fino a quando non viene fermato
        timeline.play(); //Avvia il timer
    }
    //----METODO PER AGGIORNARE IL TIMER----
    private void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        timer.setText(String.format("%02d:%02d", minutes, seconds));
    }
    //----METODO PER LA CONFERMA DI CHIUSURA DEL QUIZ----
    @FXML
    public void onTerminaButtonClicked(){
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
            timeline.stop();
            termina();
        }
    }
    //----METODO PER LA CHIUSURA DEL QUIZ----
    private synchronized void termina(){
        try {
            quiz.setTempo(remainingTime);
            controllerApplicativo.submitQuiz(quiz);
            pageLoader.loadPage(PageID.ESITO_QUIZ);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        } catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
        } catch (DataAccessException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.DB_ERROR);
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
        }
    }
    //----METODO PER IL LOGOUT----
    private void logoutProcess(){
        try {
            controllerApplicativo.clean();
            logout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
            System.exit(1);
        }
    }
}
