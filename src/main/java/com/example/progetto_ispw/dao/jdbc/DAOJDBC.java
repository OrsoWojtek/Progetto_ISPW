package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.connessione.ConnessioneJDBC;
import com.example.progetto_ispw.connessione.PersistenceConnectionManager;
import com.example.progetto_ispw.dao.DAO;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.InstanceException;

import java.sql.Connection;
//----CLASSE ASTRATTA PER DEFINIRE CLASSI DAO CHE DEVONO COMUNICARE CON DB----
public abstract class DAOJDBC implements DAO {
    protected ConnessioneJDBC connessione;
    protected Connection connection;
    protected ConnessioneJDBC getConnessione() throws ConnectionException {
        try {
            PersistenceConnectionManager istance = PersistenceConnectionManager.getInstance(); //Se è stata già aperta la connessione al db ...
            return (ConnessioneJDBC) istance;                                                  //...Allora utilizziamo quella
        } catch (InstanceException e) {
            return new ConnessioneJDBC();                                                      //Altrimenti la inizializziamo
        }
    }
}
