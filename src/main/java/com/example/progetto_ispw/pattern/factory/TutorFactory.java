package com.example.progetto_ispw.pattern.factory;

import com.example.progetto_ispw.model.Tutor;
import com.example.progetto_ispw.model.Utente;

public class TutorFactory extends UtenteFactory {
    @Override
    public Utente createUtente(String username, String password) {
        return new Tutor(username, password);
    }
}
