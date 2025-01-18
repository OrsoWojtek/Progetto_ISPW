package com.example.progetto_ispw.bean;
//----BEAN CONTENENTE INFORMAZIONI UTILI PER IL RICONOSCIMENTO DELL'UTENTE----
public class UtenteInfoBean implements Bean{
    //----ATTRIBUTI----
    private String username;
    private String role;
    //----METODI----
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {return role;}
    public String getUsername() {return username;}
}
