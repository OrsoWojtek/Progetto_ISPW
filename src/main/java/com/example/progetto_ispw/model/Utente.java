package com.example.progetto_ispw.model;

public abstract class Utente {
    private final String  username;
    private final String password;

    protected Utente(String username, String password){
        this.password = password;
        this.username = username;
    }
    public String getPassword() {return password;}
    public String getUsername() {return username;}
    public abstract String getRole();
}
