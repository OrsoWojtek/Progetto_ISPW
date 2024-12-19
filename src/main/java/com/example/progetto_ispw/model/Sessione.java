package com.example.progetto_ispw.model;

import com.example.progetto_ispw.bean.UtenteInfoBean;

//----CLASSE UTILITY PER TENERE MEMORIA DELLE CREDENZIALI DELL'UTENTE LOGGATO----
public class Sessione {
    private static UtenteInfoBean user;

    //----COSTRUTTORE PRIVATO PER IMPEDIRE L'ISTANZA----
    private Sessione(){}
    public static void setUser(UtenteInfoBean utente){ user = utente;}
    public static UtenteInfoBean getUser(){return user;}
    public static void clear(){ user = null;}
}