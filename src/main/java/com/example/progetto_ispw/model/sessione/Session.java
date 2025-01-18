package com.example.progetto_ispw.model.sessione;

import com.example.progetto_ispw.bean.Bean;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String sessionId;
    private Map<String, Bean> datiDiSessione;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.datiDiSessione = new HashMap<>();
    }

    public String getSessionId() {
        return sessionId;
    }

    public synchronized void addDato(String nome, Bean dato){
        datiDiSessione.computeIfAbsent(nome, key -> dato); //Aggiunge il dato se non esiste già
    }
    public synchronized void removeDato(String nome){
        datiDiSessione.remove(nome);
    }
    public synchronized <T extends Bean> T getDato(String nome, Class<T> classe) throws DataNotFoundException {
        // Recupera l'oggetto dalla mappa
        Bean dato = datiDiSessione.get(nome);

        // Controlla se l'oggetto è null
        if (dato == null) {
            throw new DataNotFoundException("È stata riscontrata l'assenza di un dato fondamentale nella sessione '" + this.getSessionId() + "'.");
        }

        // Controlla se l'oggetto è un'istanza del tipo richiesto
        if (classe.isInstance(dato)) {
            return classe.cast(dato); // Esegue il cast in modo sicuro
        } else {
            throw new DataSessionCastingException("Il dato associato alla chiave '" + nome + "' non è di tipo " + classe.getName());
        }
    }
}

