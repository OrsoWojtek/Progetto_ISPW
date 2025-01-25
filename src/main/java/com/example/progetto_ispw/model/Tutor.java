package com.example.progetto_ispw.model;

import com.example.progetto_ispw.constants.UserRole;

public class Tutor extends Utente{
    public Tutor(String username, String password){
        super(username,password);
    }
    @Override
    public String getRole(){
        return UserRole.TUTOR.getValue();
    }
}
