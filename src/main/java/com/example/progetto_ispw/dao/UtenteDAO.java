package com.example.progetto_ispw.dao;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.RoleNotFoundException;
import com.example.progetto_ispw.model.Utente;

//----INTERFACCIA PER DEFINIRE LA BASE PER OGNI CLASSE DAO CHE DEVE OCCUPARSI DEGLI UTENTI----
public interface UtenteDAO extends DAO{
    Utente getNewUtente(LoginInfoBean currentCred) throws DataNotFoundException, DataAccessException, RoleNotFoundException, ConnectionException; //Metodo per verificare l'esistenza delle credenziali inserite
}
