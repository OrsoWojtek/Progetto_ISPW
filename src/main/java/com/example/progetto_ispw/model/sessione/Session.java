package com.example.progetto_ispw.model.sessione;

import com.example.progetto_ispw.bean.Bean;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.model.Entity;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String sessionId;
    private Map<String, Bean> datiDiSessione;
    private Map<String, Entity> entityInSessione;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.datiDiSessione = new HashMap<>();
        this.entityInSessione = new HashMap<>();
    }

    public String getSessionId() {
        return sessionId;
    }
    public synchronized void addEntity(String nome, Entity entity){
        entityInSessione.put(nome,entity); //Aggiunge l'entità se non è presente, o la aggiorna se esiste già
    }
    public synchronized void addDato(String nome, Bean dato){
        datiDiSessione.put(nome,dato); //Aggiunge il dato se non è presente, o lo aggiorna se esiste già
    }
    public synchronized void removeDato(String nome){
        datiDiSessione.remove(nome);
    }
    public synchronized void removeEntity(String nome){
        entityInSessione.remove(nome);
    }
    public synchronized <T extends Bean> T getDato(String nome, Class<T> classe) throws DataNotFoundException {
        //Recupera l'oggetto dalla mappa
        Bean dato = datiDiSessione.get(nome);

        //Controlla se l'oggetto è null
        if (dato == null) {
            throw new DataNotFoundException("È stata riscontrata l'assenza di un dato fondamentale nella sessione '" + this.getSessionId() + "'.");
        }

        //Controlla se l'oggetto è un'istanza del tipo richiesto
        if (classe.isInstance(dato)) {
            return classe.cast(dato); //Esegue il cast in modo sicuro
        } else {
            throw new DataSessionCastingException("Il dato associato alla chiave '" + nome + "' non è di tipo " + classe.getName());
        }
    }
    public synchronized <T extends Entity> T getEntity(String nome, Class<T> classe) throws DataNotFoundException {
        //Recupera l'oggetto dalla mappa
        Entity entity = entityInSessione.get(nome);

        //Controlla se l'oggetto è null
        if (entity == null) {
            throw new DataNotFoundException("È stata riscontrata l'assenza di un dato fondamentale nella sessione '" + this.getSessionId() + "'.");
        }

        //Controlla se l'oggetto è un'istanza del tipo richiesto
        if (classe.isInstance(entity)) {
            return classe.cast(entity); //Esegue il cast in modo sicuro
        } else {
            throw new DataSessionCastingException("Il dato associato alla chiave '" + nome + "' non è di tipo " + classe.getName());
        }
    }
}

