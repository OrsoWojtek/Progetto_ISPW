package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.dao.jdbc.LoginDAOJDBC;
import com.example.progetto_ispw.exception.CredentialErrorException;
import com.example.progetto_ispw.model.Sessione;

import java.sql.SQLException;

//----CONTROLLER APPLICATIVO PER GESTIRE IL PROCESSO DI AUTENTICAZIONE----
public class LoginController {

    //----METODO DI VERIFICA DEL LOGIN----
    public void checkLogin(LoginInfoBean currLog) throws CredentialErrorException, SQLException {
        LoginDAOJDBC logdb = new LoginDAOJDBC();        //Istanziamento del DAO per il login
        logdb.isRegistered(currLog);                     //Ricerca della presenza delle credenziali nel DAO
        Sessione.setUsername(currLog.getUsername());     //Inizializzazione della sessione
    }
}
