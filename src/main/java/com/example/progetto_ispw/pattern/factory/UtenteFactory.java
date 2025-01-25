package com.example.progetto_ispw.pattern.factory;

import com.example.progetto_ispw.constants.UserRole;
import com.example.progetto_ispw.exception.RoleNotFoundException;
import com.example.progetto_ispw.model.Utente;

public abstract class UtenteFactory {
    public abstract Utente createUtente(String username, String password);

    // Factory method statico per ottenere una factory specifica
    public static UtenteFactory getFactory(String role) throws RoleNotFoundException {
        if (UserRole.STUDENTE.getValue().equalsIgnoreCase(role)) {
            return new StudenteFactory();
        } else if (UserRole.TUTOR.getValue().equalsIgnoreCase(role)) {
            return new TutorFactory();
        }
        throw new RoleNotFoundException("Ruolo non supportato: " + role);
    }
}
