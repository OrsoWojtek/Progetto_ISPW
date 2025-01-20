package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.sessione.Session;
import com.example.progetto_ispw.model.sessione.SessionManager;

//----CONTROLLER APPLICATIVO PER GESTIRE IL QUIZ----
public class QuizController {
    //----METODO PER RESTITUIRE LE INFO DEL QUIZ CORRENTE
    public QuizInfoBean getInfoQuiz() throws DataNotFoundException {
        return getQuizSession().getDato("quiz",QuizInfoBean.class);
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
        SessionManager.getInstance().getSession("course_page").removeDato("corso");
    }
    //----METODO PER RESETTARE NELLA SESSIONE LE INFO SUL QUIZ SELEZIONATO
    public void clearInfoQuiz(){
        getQuizSession().removeDato("quiz");
    }


    //----METODO PER OTTENERE LA SESSIONE RIGUARDO ALLA PAGINA DEL QUIZ
    private Session getQuizSession(){
        return SessionManager.getInstance().getSession("quiz_page");
    }
}
