package com.example.progetto_ispw.model;

public class Tutor extends Utente{
    public Tutor(String username, String password){
        super(username,password);
    }
    @Override
    public String getRole(){
        return "Tutor";
    }
}
