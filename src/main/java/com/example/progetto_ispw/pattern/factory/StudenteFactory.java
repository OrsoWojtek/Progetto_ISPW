package com.example.progetto_ispw.pattern.factory;

import com.example.progetto_ispw.model.Studente;
import com.example.progetto_ispw.model.Utente;

public class StudenteFactory extends UtenteFactory {
    @Override
    public Utente createUtente(String username, String password) {
        return new Studente(username, password);
    }
}
