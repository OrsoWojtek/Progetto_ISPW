package com.example.progetto_ispw.model;

public class Tutor extends Utente{
    public Tutor(String username, String password){
        super(username,password);
    }
    @Override
    public boolean isAuthorized(String resource, String cause){
        //Il tutor è autorizzato ad accedere a risorse "CORSO" e "QUIZ", in accesso e modifica
        return (resource.equalsIgnoreCase("CORSO") || resource.equalsIgnoreCase("QUIZ"))&&(cause.equalsIgnoreCase("ACCESS")||cause.equalsIgnoreCase("MODIFY"));
    }
    @Override
    public String getRole(){
        return "Tutor";
    }
}
