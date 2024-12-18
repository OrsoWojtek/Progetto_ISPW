package com.example.progetto_ispw.model;

public abstract class Utente {
    private String  username;
    private String password;

    public Utente(String username, String password){
        this.password = password;
        this.username = username;
    }
    public String getPassword() {return password;}
    public String getUsername() {return username;}

    public abstract boolean isAuthorized(String resource, String cause);
    public abstract String getRole();
}
