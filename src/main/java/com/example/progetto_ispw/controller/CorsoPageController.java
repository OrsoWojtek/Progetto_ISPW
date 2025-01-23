package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.*;
import com.example.progetto_ispw.constants.DataID;
import com.example.progetto_ispw.constants.SessionID;
import com.example.progetto_ispw.dao.jdbc.QuizDAOJDBC;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.Quesito;
import com.example.progetto_ispw.model.Quiz;
import com.example.progetto_ispw.model.Risposta;
import com.example.progetto_ispw.model.sessione.Session;
import com.example.progetto_ispw.model.sessione.SessionManager;

import java.util.ArrayList;
import java.util.List;

//----CONTROLLER APPLICATIVO PER GESTIRE IL CORSO----
public class CorsoPageController {
    //----METODO PER RICERCARE QUALI SONO I QUIZ DISPONIBILI NEL CORSO----
    public List<QuizInfoBean> getQuizDisponibili(CorsoInfoBean corso, UtenteInfoBean utente) throws ConnectionException, DataAccessException {
        QuizDAOJDBC dao = new QuizDAOJDBC();
        List<QuizInfoBean> catalogoQuiz = new ArrayList<>();
        List<Quiz> quizzes = dao.getQuizzes(corso, utente);

        // Conversione dei quiz in oggetti QuizInfoBean
        for (Quiz quiz : quizzes) {
            SessionManager.getInstance().createSession(SessionID.CATALOGO_QUIZ).addEntity(quiz.getTitolo(), quiz); //Creazione di una sessione dedicata per contenere il catalogo dei quiz estratti dal db
            QuizInfoBean quizInfoBean = new QuizInfoBean();
            quizInfoBean.setTitolo(quiz.getTitolo());
            quizInfoBean.setDurata(quiz.getDurata());
            quizInfoBean.setDifficolta(quiz.getDifficolta());
            quizInfoBean.setArgomenti(quiz.getArgomenti());
            quizInfoBean.setPunteggio(quiz.getPunteggio());
            quizInfoBean.setPunteggioStudente(quiz.getScoreUtente());

            // Conversione dei quesiti
            List<QuesitoInfoBean> quesiti = getQuesitoInfoBeans(quiz);

            quizInfoBean.setQuesiti(quesiti);
            quizInfoBean.setNumeroDomandeBean();
            catalogoQuiz.add(quizInfoBean);
        }

        return catalogoQuiz;
    }
    //----METODO PER LA CONVERSIONE DEI QUESITI----
    private List<QuesitoInfoBean> getQuesitoInfoBeans(Quiz quiz) {
        List<QuesitoInfoBean> quesiti = new ArrayList<>();
        for (Quesito quesito : quiz.getQuesiti()) {
            QuesitoInfoBean quesitoInfoBean = new QuesitoInfoBean();
            quesitoInfoBean.setDomanda(quesito.getDomanda());
            quesitoInfoBean.setPunti(quesito.getPunti());

            // Conversione delle risposte
            List<RispostaInfoBean> risposte = new ArrayList<>();
            for (Risposta risposta : quesito.getRisposte()) {
                RispostaInfoBean rispostaInfoBean = new RispostaInfoBean();
                rispostaInfoBean.setTesto(risposta.getTesto());
                rispostaInfoBean.setCorretta(risposta.isCorretta());
                rispostaInfoBean.setTicked(risposta.isTicked());
                risposte.add(rispostaInfoBean);
            }

            quesitoInfoBean.setRisposte(risposte);
            quesiti.add(quesitoInfoBean);
        }
        return quesiti;
    }

    //----METODO PER PULIRE CONNESSIONE AL DB E SESSIONE AL LOGOUT----
    public void clean() throws ConnectionException {
        SessionManager.getInstance().invalidateSessions(); //Formatto le sessioni
        Connessione conn = Connessione.getInstance();
        conn.closeConnection(); //Chiudo definitivamente la connessione con il db
    }
    //----METODO PER RESETTARE NELLA SESSIONE LE INFO SUL CORSO SELEZIONATO
    public void clearInfoCourse(){
        Session session = SessionManager.getInstance().getSession(SessionID.COURSE_PAGE);
        session.removeDato(DataID.CORSO);
        session.removeEntity(DataID.CORSO.getValue());
    }
    //----METODO PER RESTITUIRE LE INFO SUL CORSO SELEZIONATO
    public CorsoInfoBean getInfoCourse() throws DataNotFoundException {
        return SessionManager.getInstance().getSession(SessionID.COURSE_PAGE).getDato(DataID.CORSO,CorsoInfoBean.class);
    }
    //----METODO PER RESTITUIRE LE INFO DELL'UTENTE CORRENTE
    public UtenteInfoBean getInfoUser() throws DataNotFoundException {
        return SessionManager.getInstance().getSession(SessionID.LOGIN).getDato(DataID.UTENTE,UtenteInfoBean.class);
    }
    //----METODO PER MEMORIZZARE NELLA SESSIONE IL QUIZ SELEZIONATO
    public void setInfoQuiz(QuizInfoBean currentQuiz) throws DataNotFoundException {
        SessionManager istance = SessionManager.getInstance();
        Quiz quizSelezionato = istance.getSession(SessionID.CATALOGO_QUIZ).getEntity(currentQuiz.getTitolo(), Quiz.class);
        istance.createSession(SessionID.QUIZ_PAGE).addDato(DataID.QUIZ,currentQuiz);
        istance.createSession(SessionID.QUIZ_PAGE).addEntity(DataID.QUIZ.getValue(), quizSelezionato);
    }
}
