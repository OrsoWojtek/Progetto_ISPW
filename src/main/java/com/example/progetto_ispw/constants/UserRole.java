package com.example.progetto_ispw.constants;
//----CLASSE ENUM CHE RACCOGLIE UN INSIEME DI IDENTIFICATORI UNIVOCI PER I DIVERSI RUOLI CHE PUÒ AVERE UN UTENTE----
public enum UserRole {
    STUDENTE("studente"),   //Identificatore per il ruolo di studente
    TUTOR("tutor");   //Identificatore per il ruolo di tutor
    private final String value;
    UserRole(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
