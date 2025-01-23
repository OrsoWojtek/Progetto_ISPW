package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.AnsweringProcessInfoBean;
import com.example.progetto_ispw.bean.QuesitoInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.RispostaInfoBean;
import com.example.progetto_ispw.constants.DataID;
import com.example.progetto_ispw.constants.SessionID;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.NotFilledQuestionException;
import com.example.progetto_ispw.model.Quiz;
import com.example.progetto_ispw.model.sessione.Session;
import com.example.progetto_ispw.model.sessione.SessionManager;

//----CONTROLLER APPLICATIVO PER GESTIRE IL QUIZ----
public class QuizController {
    //----METODO CHE CONTROLLA LA PRESENZA DI ALMENO UN QUESITO DEL QUIZ SENZA UNA RISPOSTA SELEZIONATA DALL'UTENTE----
    public void isFullyFilled(QuizInfoBean quiz) throws NotFilledQuestionException {
        for (QuesitoInfoBean quesito : quiz.getQuesiti()) {
            boolean allNotTicked = true;

            for (RispostaInfoBean risposta : quesito.getRisposte()) {
                if (risposta.isTicked()) {
                    allNotTicked = false;
                    break; //Esci dal ciclo interno appena trovi una risposta di un quesito che è stata selezionata
                }
            }

            if (allNotTicked) {
                //Lancia l'eccezione appena trovi un quesito con tutte le risposte non selezionate
                throw new NotFilledQuestionException("Trovato un quesito con tutte le risposte non ticked.");
            }
        }
        //Se il metodo termina senza lanciare l'eccezione, significa che non esiste un quesito con tutte le risposte non ticked
    }
    //----METODO PER RESTITUIRE LE INFO DEL QUIZ CORRENTE
    public QuizInfoBean getInfoQuiz() throws DataNotFoundException {
        return getQuizSession().getDato(DataID.QUIZ,QuizInfoBean.class);
    }
    //----METODO PER PULIRE CONNESSIONE AL DB E SESSIONE AL LOGOUT----
    public void clean() throws ConnectionException {
        SessionManager.getInstance().invalidateSessions(); //Formatto le sessioni
        Connessione conn = Connessione.getInstance();
        conn.closeConnection(); //Chiudo definitivamente la connessione con il db
    }
    //----METODO PER PULIRE LE SESSIONI SUCCESSIVE A QUELLA DI LOGIN----
    public void clearOtherInfo(){
        clearInfoQuiz();
        SessionManager istance = SessionManager.getInstance();
        istance.getSession(SessionID.COURSE_PAGE).removeDato(DataID.CORSO);
        istance.getSession(SessionID.COURSE_PAGE).removeEntity(DataID.CORSO.getValue());
    }
    //----METODO PER RESETTARE NELLA SESSIONE LE INFO SUL QUIZ SELEZIONATO
    public void clearInfoQuiz(){
        getQuizSession().removeDato(DataID.QUIZ);
        getQuizSession().removeEntity(DataID.QUIZ.getValue());
    }
    //----METODO PER OTTENERE LA SESSIONE RIGUARDO ALLA PAGINA DEL QUIZ
    private Session getQuizSession(){
        return SessionManager.getInstance().getSession(SessionID.QUIZ_PAGE);
    }
    //----METODO PER SPUNTARE LA RISPOSTA SELEZIONATA----
    public void tickAnswer(AnsweringProcessInfoBean answer) {
        QuesitoInfoBean quesito = answer.getQuesitoInfoBean();
        String rispostaSelezionata = answer.getRisposta();
        for(RispostaInfoBean risposta : quesito.getRisposte()){
            if(risposta.getTesto().equals(rispostaSelezionata)){
                risposta.setTicked(true);
                break;
            }
        }
    }
    //----METODO PER TOGLIERE LA SPUNTA DA UNA RISPOSTA----
    public void untickAnswer(AnsweringProcessInfoBean answer) {
        QuesitoInfoBean quesito = answer.getQuesitoInfoBean();
        String rispostaPrecedente = answer.getRisposta();
        for(RispostaInfoBean risposta : quesito.getRisposte()){
            if(risposta.getTesto().equals(rispostaPrecedente)){
                risposta.setTicked(false);
                break;
            }
        }
    }
    //----METODO PER SOTTOPORRERE IL QUIZ----
    public void submitQuiz(QuizInfoBean quiz) throws DataNotFoundException {
        for (QuesitoInfoBean quesito : quiz.getQuesiti()) {
            for (RispostaInfoBean risposta : quesito.getRisposte()){
                if(risposta.isCorretta()&&risposta.isTicked()) {
                    quiz.setPunteggioStudente(quesito.getPunti());
                }
            }
        }
        updateQuiz(quiz.getPunteggioStudente());
        //aggiungi l'aggiornamento dei punti dello studente relativi al quiz sul db (usa l'entity)
        //int i;
    }
    //----METODO PER AGGIORNARE L'ENTITÀ QUIZ NELLA SESSIONE----
    private void updateQuiz(int punteggioStudente) throws DataNotFoundException {
        Quiz quiz = getQuizSession().getEntity(DataID.QUIZ.getValue(), Quiz.class);
        if(quiz.getScoreUtente()<punteggioStudente) { //Se il punteggio ottenuto sul momento dello studente è maggiore dal punteggio che ha precedentemente ottenuto allo stesso quiz...
            quiz.updateScoreUtente(punteggioStudente); //...Allora aggiorna il punteggio dello studente
        }
    }
}
