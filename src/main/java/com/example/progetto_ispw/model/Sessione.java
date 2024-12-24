package com.example.progetto_ispw.model;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;


//----CLASSE SINGLETON PER MANTENERE TRACCIA DELLA SESSIONE CORRENTE DELL'UTENTE----
public class Sessione {
    private static final Sessione instance = new Sessione(); // Singleton
    private UtenteInfoBean user;
    private CorsoInfoBean course;

    // Costruttore privato per impedire l'instanziazione
    private Sessione() {}

    public static Sessione getInstance() {
        return instance;
    }

    // Metodi per gestire l'utente
    public void setUser(UtenteInfoBean utente) {
        this.user = utente;
    }

    public UtenteInfoBean getUser() {
        return user;
    }

    // Metodi per gestire il corso
    public void setCourse(CorsoInfoBean corso) {
        this.course = corso;
    }

    public CorsoInfoBean getCourse() {
        return course;
    }

    // Metodo per resettare la sessione
    public void clear() {
        user = null;
        course = null;
    }
}