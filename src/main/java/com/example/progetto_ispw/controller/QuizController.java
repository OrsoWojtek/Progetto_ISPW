package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.bean.*;
import com.example.progetto_ispw.connessione.PersistenceConnectionManager;
import com.example.progetto_ispw.constants.DataID;
import com.example.progetto_ispw.constants.SessionID;
import com.example.progetto_ispw.dao.CorsoDAO;
import com.example.progetto_ispw.dao.QuizDAO;
import com.example.progetto_ispw.dao.TipologiaDAO;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.model.Quiz;
import com.example.progetto_ispw.model.sessione.Session;
import com.example.progetto_ispw.model.sessione.SessionManager;

//----CONTROLLER APPLICATIVO PER GESTIRE IL QUIZ----
public class QuizController {
    //___________________________METODI CHE HANNO STRETTAMENTE A CHE FARE CON LA SESSIONE__________________________________
    //----METODO PER RESTITUIRE LE INFO DEL QUIZ CORRENTE
    public QuizInfoBean getInfoQuiz() throws DataNotFoundException {
        return getQuizSession().getDato(DataID.QUIZ,QuizInfoBean.class);
    }
    //----METODO PER PULIRE CONNESSIONE E SESSIONE AL LOGOUT----
    public void clean() throws ConnectionException {
        SessionManager.getInstance().invalidateSessions(); //Formatto le sessioni
        try {
            PersistenceConnectionManager conn = PersistenceConnectionManager.getInstance();
            conn.closeConnection(); //Chiudo definitivamente la connessione con la persistenza
        }catch (InstanceException e){
            throw new ConnectionException("Si è verificato un errore inatteso nella ricerca della connessione al livello di persistenza");
        }
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
        getQuizSession().removeDato(DataID.ERRORI);
    }
    //----METODO PER OTTENERE LA SESSIONE RIGUARDO ALLA PAGINA DEL QUIZ
    private Session getQuizSession(){
        return SessionManager.getInstance().getSession(SessionID.QUIZ_PAGE);
    }



    //___________________________METODI 'PIÙ LEGATI AL CASO D'USO'__________________________________
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
    //----METODO PER SPUNTARE LA RISPOSTA SELEZIONATA----
    public void tickAnswer(AnsweringProcessInfoBean answer) {
        QuesitoInfoBean quesito = answer.getQuesitoInfoBean();
        String rispostaSelezionata = answer.getRisposta();
        for(RispostaInfoBean risposta : quesito.getRisposte()){
            risposta.setTicked(risposta.getTesto().equals(rispostaSelezionata)); //Imposta 'ticked' la risposta appena selezionata, togliendolo alle altre
        }
    }

    //----METODO PER SOTTOPORRERE IL QUIZ----
    public void submitQuiz(QuizInfoBean quiz) throws DataNotFoundException, ConnectionException, DataAccessException {
        quiz.setPunteggioStudente(-quiz.getPunteggioStudente()); //Resettiamo il punteggio dello studente
        ErroriQuizInfoBean errori = new ErroriQuizInfoBean();
        for (QuesitoInfoBean quesito : quiz.getQuesiti()) {
            for (RispostaInfoBean risposta : quesito.getRisposte()){
                if(risposta.isTicked()){
                    if(risposta.isCorretta()){
                        quiz.setPunteggioStudente(quesito.getPunti());
                    } else {
                        errori.setErrore(quesito.getDomanda(),risposta.getTesto());
                    }
                    break;
                }
            }
        }
        getQuizSession().addDato(DataID.ERRORI,errori); //Aggiungo il bean con gli errori alla sessione della pagina del quiz
        try {
            updateQuiz(quiz.getPunteggioStudente());
        } catch (UpdateDataException e) { //Update in persistenza non andato a buon fine
            throw new DataNotFoundException("Non è stato possibile salvare il nuovo punteggio ottenuto.");
        }
        try {
            addNotifica();
        } catch (UpdateDataException e) {
            throw new DataNotFoundException("Non è stato possibile mandare la notifica al tutor.");
        }
    }
    //----METODO PER AGGIUNGERE UNA NOTIFICA AL TUTOR DEL CORSO----
    private void addNotifica() throws ConnectionException, DataNotFoundException, DataAccessException, UpdateDataException {
        CorsoDAO dao = (CorsoDAO) TipologiaDAO.CORSO.getDao();
        UtenteInfoBean user = SessionManager.getInstance().getSession(SessionID.LOGIN).getDato(DataID.UTENTE, UtenteInfoBean.class);
        CorsoInfoBean corso = SessionManager.getInstance().getSession(SessionID.COURSE_PAGE).getDato(DataID.CORSO, CorsoInfoBean.class);
        QuizInfoBean quiz = getInfoQuiz();
        dao.updateNotifiche(user,corso,quiz);
    }

    //----METODO PER AGGIORNARE L'ENTITÀ QUIZ NELLA SESSIONE----
    private void updateQuiz(int punteggioStudente) throws DataNotFoundException, ConnectionException, UpdateDataException, DataAccessException {
        Quiz quiz = getQuizSession().getEntity(DataID.QUIZ.getValue(), Quiz.class);
        if(quiz.getScoreUtente()<punteggioStudente) { //Se il punteggio ottenuto sul momento dello studente è maggiore dal punteggio che ha precedentemente ottenuto allo stesso quiz...
            quiz.updateScoreUtente(punteggioStudente); //...Allora aggiorna il punteggio dello studente
            QuizDAO dao = (QuizDAO) TipologiaDAO.QUIZ.getDao();
            UtenteInfoBean utente = SessionManager.getInstance().getSession(SessionID.LOGIN).getDato(DataID.UTENTE, UtenteInfoBean.class);
            CorsoInfoBean corso = SessionManager.getInstance().getSession(SessionID.COURSE_PAGE).getDato(DataID.CORSO, CorsoInfoBean.class);
            dao.updateScore(quiz,utente,corso); //Aggiorna il punteggio ottenuto dal dao
        }
    }
    //----METODO PER SCOPRIRE GLI ERRORI COMMESSI DALL'UTENTE NEL QUIZ----
    public ErroriQuizInfoBean getErrori() throws DataNotFoundException {
        return getQuizSession().getDato(DataID.ERRORI,ErroriQuizInfoBean.class);
    }
}
