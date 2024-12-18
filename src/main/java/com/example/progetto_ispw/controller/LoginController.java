package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.dao.jdbc.UtenteDAOJDBC;
import com.example.progetto_ispw.exception.CredentialErrorException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.RoleNotFoundException;
import com.example.progetto_ispw.model.Sessione;
import com.example.progetto_ispw.model.Utente;

//----CONTROLLER APPLICATIVO PER GESTIRE IL PROCESSO DI AUTENTICAZIONE----
public class LoginController {

    //----METODO DI VERIFICA DEL LOGIN----
    public UtenteInfoBean checkLogin(LoginInfoBean currLog) throws CredentialErrorException, DataAccessException, RoleNotFoundException {
        UtenteDAOJDBC db = new UtenteDAOJDBC();                     //Istanziamento del DAO per il login
        UtenteInfoBean utenteInfo = new UtenteInfoBean();          //Generato il bean per contenere le informazioni necessarie da trasferire
        Utente utente = db.getNewUtente(currLog);                   //Generato il nuovo utente
        utenteInfo.setRole(utente.getRole());                      //Prelevata l'informazione sul ruolo e username
        utenteInfo.setUsername(utente.getUsername());
        Sessione.setUsername(utente.getUsername());                 //Inizializzazione della sessione
        return utenteInfo;
    }
}
