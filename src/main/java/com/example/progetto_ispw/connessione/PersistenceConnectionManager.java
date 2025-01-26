package com.example.progetto_ispw.connessione;

import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.InstanceException;

//----CLASSE ASTRATTA PER DEFINIRE CLASSI CHE SI OCCUPANO DELLA RAPPRESENTAZIONE DELLE CONNESSIONE AL LIVELLO DI PERSISTENZA----
public abstract class PersistenceConnectionManager {
    protected static PersistenceConnectionManager instance;

    //Metodo per ottenere l'istanza del singleton
    public static synchronized PersistenceConnectionManager getInstance() throws InstanceException{
        if (instance == null) {
            throw new InstanceException("Istanza non inizializzata. Usa un costruttore di una classe figlio.");
        }
        return instance;
    }

    //Metodo per inizializzare l'istanza (da usare solo nelle classi concrete)
    protected static void initializeInstance(PersistenceConnectionManager manager) {
        if (instance == null) {
            instance = manager;
        } else {
            throw new IllegalStateException("Istanza già inizializzata");
        }
    }
    //Metodo astratto per aprire la connessione
    protected abstract void openConnection() throws ConnectionException;

    //Metodo astratto per chiudere la connessione
    public abstract void closeConnection() throws ConnectionException;
}