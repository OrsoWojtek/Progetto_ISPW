package com.example.progetto_ispw.model;

import com.example.progetto_ispw.constants.UserRole;

public class Studente extends Utente{
    public Studente(String username, String password){
        super(username,password);
    }
    @Override
    public String getRole(){
        return UserRole.STUDENTE.getValue();
    }
}
