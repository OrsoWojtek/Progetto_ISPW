package com.example.progetto_ispw.model;

public class Studente extends Utente{
    public Studente(String username, String password){
        super(username,password);
    }
    @Override
    public boolean isAuthorized(String resource, String cause){
        //Lo studente è autorizzato ad accedere a risorse "CORSO" e "QUIZ", ma non a modificarle
        return (resource.equalsIgnoreCase("CORSO") || resource.equalsIgnoreCase("QUIZ"))&&cause.equalsIgnoreCase("ACCESS");
    }
    @Override
    public String getRole(){
        return "Studente";
    }
}
