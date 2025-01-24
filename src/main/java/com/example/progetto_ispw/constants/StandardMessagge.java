package com.example.progetto_ispw.constants;

//----CLASSE ENUM CHE RACCOGLIE UN INSIEME DI IDENTIFICATORI UNIVOCI PER MESSAGGI STANDARD USATI NEL PROGETTO----
public enum StandardMessagge {
    CASTING("Si è presentato un errore nel casting di un qualche dato conservato nella sessione."),
    MAINTENANCE("Al momento non è possibile utilizzare questa funzionalità.\nCi dispiace per il disagio."),
    CREDENTIAL("Una o più credenziali non sono state inserite.");
    private final String value;
    StandardMessagge(String value){this.value = value;}
    public String getValue(){
        return value;
    }
}
