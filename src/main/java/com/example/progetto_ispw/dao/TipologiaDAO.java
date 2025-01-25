package com.example.progetto_ispw.dao;

import com.example.progetto_ispw.dao.jdbc.CorsoDAOJDBC;
import com.example.progetto_ispw.dao.jdbc.QuizDAOJDBC;
import com.example.progetto_ispw.dao.jdbc.UtenteDAOJDBC;
import com.example.progetto_ispw.exception.ConnectionException;

//----CLASSE ENUM CHE RACCOGLIE UN INSIEME DI IDENTIFICATORI UNIVOCI PER I DIVERSI TIPI DI DAO DA USARE----

public enum TipologiaDAO {
    UTENTE(UtenteDAOJDBC.class), //Identificatore per i dao che si occupano degli utenti
    CORSO(CorsoDAOJDBC.class),  //Identificatore per i dao che si occupano dei corsi
    QUIZ(QuizDAOJDBC.class);    //Identificatore per i dao che si occupano dei quiz
    private final Class<? extends DAO> daoClass;
    TipologiaDAO(Class<? extends DAO> daoClass) {
        this.daoClass = daoClass;
    }
    //Metodo che restituisce il DAO specifico (es. UtenteDAO, CorsoDAO, ecc)
    public DAO getDao() throws ConnectionException {
        try {
            return daoClass.getDeclaredConstructor().newInstance(); //Restituisce l'oggetto concreto, senza bisogno di casting
        } catch (Exception e) {
            throw new ConnectionException("Errore nella creazione del DAO");
        }
    }
}