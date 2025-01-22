package com.example.progetto_ispw.constants;

//----CLASSE ENUM CHE RACCOGLIE UN INSIEME DI IDENTIFICATORI UNIVOCI PER DIVERSI DATI DA MANTENERE IN UNA SESSIONE----
public enum DataID {
    //Costanti definite nella enum, ognuna rappresenta un identificatore unico.
    UTENTE("utente"),    //Identificatore per i dati che si riferiscono ad un 'utente'
    CORSO("corso"),     //Identificatore per i dati che si riferiscono ad un 'corso'
    QUIZ("quiz");        //Identificatore per i dati che si riferiscono ad un 'quiz'
    private final String value;
    DataID(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
