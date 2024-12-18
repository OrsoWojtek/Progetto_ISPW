package com.example.progetto_ispw.model;

public class Studente extends Utente{
    public Studente(String username, String password){
        super(username,password);
    }
    @Override
    public String getRole(){
        return "Studente";
    }
}
