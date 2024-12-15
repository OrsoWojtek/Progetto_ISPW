package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.dao.jdbc.LoginDAO_JDBC;
import com.example.progetto_ispw.exception.CredentialErrorException;
import com.example.progetto_ispw.model.Sessione;

import java.sql.SQLException;

//----CONTROLLER APPLICATIVO PER GESTIRE IL PROCESSO DI AUTENTICAZIONE----
public class LoginController {

    //----METODO DI VERIFICA DEL LOGIN----
    public void checkLogin(LoginInfoBean curr_log) throws CredentialErrorException, SQLException {
        LoginDAO_JDBC logdb = new LoginDAO_JDBC();        //Istanziamento del DAO per il login
        logdb.isRegistered(curr_log);                     //Ricerca della presenza delle credenziali nel DAO
        Sessione.setUsername(curr_log.getUsername());     //Inizializzazione della sessione
    }
}
