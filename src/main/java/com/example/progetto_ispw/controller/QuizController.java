package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.sessione.SessionManager;

//----CONTROLLER APPLICATIVO PER GESTIRE IL QUIZ----
public class QuizController {
    //----METODO PER RESTITUIRE LE INFO DEL QUIZ CORRENTE
    public QuizInfoBean getInfoQuiz() throws DataNotFoundException {
        return SessionManager.getInstance().getSession("quiz_page").getDato("quiz",QuizInfoBean.class);
    }
    //----METODO PER PULIRE CONNESSIONE AL DB E SESSIONE AL LOGOUT----
    public void clean() throws ConnectionException {
        SessionManager.getInstance().invalidateSessions(); //Formatto le sessioni
        Connessione conn = Connessione.getInstance();
        conn.closeConnection(); //Chiudo definitivamente la connessione con il db
    }
    //----METODO PER PULIRE LE SESSIONI SUCCESSIVE A QUELLA DI LOGIN----
    public void clearOtherInfo(){
        SessionManager istance = SessionManager.getInstance();
        istance.getSession("quiz_page").removeDato("quiz");
        istance.getSession("course_page").removeDato("corso");
    }
    //----METODO PER RESETTARE NELLA SESSIONE LE INFO SUL QUIZ SELEZIONATO
    public void clearInfoQuiz(){
        SessionManager.getInstance().getSession("quiz_page").removeDato("quiz");
    }
}
