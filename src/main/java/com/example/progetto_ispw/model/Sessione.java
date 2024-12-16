package com.example.progetto_ispw.model;

//----CLASSE UTILITY PER TENERE MEMORIA DELLE CREDENZIALI DELL'UTENTE LOGGATO----
public class Sessione {
    private static String currentUsername;

    //----COSTRUTTORE PRIVATO PER IMPEDIRE L'ISTANZA----
    private Sessione(){}

    public static void setUsername(String username) {
        currentUsername = username;
    }
    public static String getUsername() {
        return currentUsername;
    }
    public static void clear(){
        currentUsername = null;
    }
}