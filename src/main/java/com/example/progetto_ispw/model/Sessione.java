package com.example.progetto_ispw.model;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;


//----CLASSE SINGLETON PER MANTENERE TRACCIA DELLA SESSIONE CORRENTE DELL'UTENTE----
public class Sessione {
    private static volatile Sessione instance; // Volatile per evitare problemi di visibilità

    private UtenteInfoBean user;
    private CorsoInfoBean course;

    protected Sessione() {}

    public static Sessione getInstance() {
        if (instance == null) {
            synchronized (Sessione.class) {
                if (instance == null) {
                    instance = new Sessione();
                }
            }
        }
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