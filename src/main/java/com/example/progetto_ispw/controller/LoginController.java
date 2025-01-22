package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.dao.jdbc.UtenteDAOJDBC;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.RoleNotFoundException;
import com.example.progetto_ispw.model.Utente;
import com.example.progetto_ispw.model.sessione.SessionManager;

//----CONTROLLER APPLICATIVO PER GESTIRE IL PROCESSO DI AUTENTICAZIONE----
public class LoginController{

    //----METODO DI VERIFICA DEL LOGIN----
    public void checkLogin(LoginInfoBean currLog) throws DataNotFoundException, DataAccessException, RoleNotFoundException, ConnectionException {
        UtenteDAOJDBC db = new UtenteDAOJDBC();                     //Istanziamento del DAO per il login
        UtenteInfoBean utenteInfo = new UtenteInfoBean();          //Generato il bean per contenere le informazioni necessarie da trasferire
        Utente utente = db.getNewUtente(currLog);                   //Prelevato il nuovo utente
        utenteInfo.setRole(utente.getRole());                      //Prelevata l'informazione sul ruolo e username
        utenteInfo.setUsername(utente.getUsername());
        SessionManager istance = SessionManager.getInstance();
        istance.createSession("login").addDato("utente",utenteInfo); //Creata la sessione di login e inserito il bean con le info dell'utente
        istance.getSession("login").addEntity("utente",utente); //Inserita anche l'entità
    }
}
