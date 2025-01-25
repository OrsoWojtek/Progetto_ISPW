package com.example.progetto_ispw.dao.filesystem;

import com.example.progetto_ispw.connessione.ConnessioneFS;
import com.example.progetto_ispw.connessione.PersistenceConnectionManager;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.InstanceException;

import java.io.BufferedReader;
import java.io.BufferedWriter;

//----CLASSE ASTRATTA PER DEFINIRE CLASSI DAO CHE DEVONO COMUNICARE CON FILESYSTEM----
public abstract class DAOFS {
    protected ConnessioneFS connessione;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    //-----METODO PER OTTENERE LA CONNESSIONE AL FILESYSTEM (FILE CSV)
    protected void getConnessione(String fileName, boolean isWriteMode) throws ConnectionException {
        try {
            PersistenceConnectionManager istance = PersistenceConnectionManager.getInstance();
            connessione = (ConnessioneFS) istance;  //Se è stata già inizializzata la connessione, la riusiamo
        } catch (InstanceException e) {
            connessione = new ConnessioneFS(fileName);  //Altrimenti, la creiamo
        }

        //Se è necessario scrivere, imposta la modalità di scrittura
        if (isWriteMode) {
            connessione.setWriter(true);  //Modalità append (aggiungi dati)
            writer = connessione.getWriter();
        } else {
            reader = connessione.getReader();  //Modalità lettura
        }

    }

    //----METODO PER CHIUDERE LA CONNESSIONE----
    public void closeConnection() throws ConnectionException {
        if (connessione != null) {
            if (reader != null) {
                connessione.closeReader();
            }
            if (writer != null) {
                connessione.closeWriter();
            }
            connessione.closeConnection();
        }
    }
}
