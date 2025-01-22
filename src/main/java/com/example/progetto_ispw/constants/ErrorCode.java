package com.example.progetto_ispw.constants;

//----CLASSE ENUM CHE RAPPRESENTA UNA RACCOLTA DI MESSAGGI DI ERRORE STANDARDIZZATI PER L'APPLICAZIONE----
public enum ErrorCode {
    CREDENTIAL_NOT_FOUND("Credenziali assenti"),
    DB_ERROR("Errore DB"),
    UNDEF_ROLE("Ruolo indefinito"),
    CREDENTIAL_ERROR("Credenziali errate"),
    MAINTENANCE("Funzionalità in manutenzione"),
    CONNECTION("Errore connessione"),
    PAGE_NOT_FOUND("Pagina non trovata"),
    SESSION("Errore di sessione"),
    CASTING("Errore di casting"),
    QUIZ_NOT_FOUND("Quiz non trovati");

    private final String message;
    ErrorCode(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
