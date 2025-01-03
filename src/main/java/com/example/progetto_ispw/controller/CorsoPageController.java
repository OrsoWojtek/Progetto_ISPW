package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.model.Sessione;

//----CONTROLLER APPLICATIVO PER GESTIRE IL CORSO----
public class CorsoPageController {

    //----METODO PER PULIRE CONNESSIONE AL DB E SESSIONE AL LOGOUT----
    public void clean() throws ConnectionException {
        Sessione.getInstance().clear(); //Cancello le informazioni riguardanti la sessione
        Connessione conn = Connessione.getInstance();
        conn.closeConnection(); //Chiudo definitivamente la connessione con il db
    }
    //----METODO PER RESETTARE NELLA SESSIONE LE INFO SUL CORSO SELEZIONATO
    public void clearInfoCourse(){
        Sessione.getInstance().setCourse(null);
    }
    //----METODO PER RESTITUIRE LE INFO SUL CORSO SELEZIONATO
    public CorsoInfoBean getInfoCourse(){
        return Sessione.getInstance().getCourse();
    }
    //----METODO PER RESTITUIRE LE INFO DELL'UTENTE CORRENTE
    public UtenteInfoBean getInfoUser(){return Sessione.getInstance().getUser();}
}
