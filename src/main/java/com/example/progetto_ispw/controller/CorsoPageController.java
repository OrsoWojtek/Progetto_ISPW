package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.*;
import com.example.progetto_ispw.dao.jdbc.QuizDAOJDBC;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.Quesito;
import com.example.progetto_ispw.model.Quiz;
import com.example.progetto_ispw.model.Risposta;
import com.example.progetto_ispw.model.sessione.SessionManager;

import java.util.ArrayList;
import java.util.List;

//----CONTROLLER APPLICATIVO PER GESTIRE IL CORSO----
public class CorsoPageController {
    //----METODO PER RICERCARE QUALI SONO I QUIZ DISPONIBILI NEL CORSO----
    public List<QuizInfoBean> getQuizDisponibili(CorsoInfoBean corso) throws ConnectionException, DataAccessException {
        QuizDAOJDBC dao = new QuizDAOJDBC();
        List<QuizInfoBean> catalogoQuiz = new ArrayList<>();
        List<Quiz> quizzes = dao.getQuizzes(corso);

        // Conversione dei quiz in oggetti QuizInfoBean
        for (Quiz quiz : quizzes) {
            QuizInfoBean quizInfoBean = new QuizInfoBean();
            quizInfoBean.setTitolo(quiz.getTitolo());
            quizInfoBean.setDurata(quiz.getDurata());
            quizInfoBean.setDifficolta(quiz.getDifficolta());
            quizInfoBean.setArgomenti(quiz.getArgomenti());
            quizInfoBean.setPunteggio(quiz.getPunteggio());

            // Conversione dei quesiti
            List<QuesitoInfoBean> quesiti = getQuesitoInfoBeans(quiz);

            quizInfoBean.setQuesiti(quesiti);
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
        SessionManager.getInstance().getSession("course_page").removeDato("corso");
    }
    //----METODO PER RESTITUIRE LE INFO SUL CORSO SELEZIONATO
    public CorsoInfoBean getInfoCourse() throws DataNotFoundException {
        return SessionManager.getInstance().getSession("course_page").getDato("corso",CorsoInfoBean.class);
    }
    //----METODO PER RESTITUIRE LE INFO DELL'UTENTE CORRENTE
    public UtenteInfoBean getInfoUser() throws DataNotFoundException {
        return SessionManager.getInstance().getSession("login").getDato("utente",UtenteInfoBean.class);
    }
}
