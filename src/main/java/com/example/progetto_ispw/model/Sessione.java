package com.example.progetto_ispw.model;

//----CLASSE UTILITY PER TENERE MEMORIA DELLE CREDENZIALI DELL'UTENTE LOGGATO----
public class Sessione {
    private static String current_username;

    //----COSTRUTTORE PRIVATO PER IMPEDIRE L'ISTANZA----
    private Sessione(){}

    public static void setUsername(String username) {
        current_username = username;
    }
    public static String getUsername() {
        return current_username;
    }
    public static void clear(){
        current_username = null;
    }
}