package com.example.progetto_ispw.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErroriQuizInfoBean implements Bean{
    //----ATTRIBUTI----
    private Map<String,String> errori = new LinkedHashMap<>(); //Uso LinkedHashMap per mantenere l'ordine di inserimento dei dati
    //----METODI----
    public Map<String, String> getErrori() {
        return errori;
    }
    public void setErrori(Map<String, String> errori) {
        this.errori = errori;
    }
    public void setErrore(String domanda, String errore) {
        this.errori.put(domanda, errore);
    }
    @Override
    public String toString(){
        StringBuilder resoconto = new StringBuilder("Non ci sono stati errori. Se non hai ottenuto il punteggio massimo potresti aver lasciato qualche domanda senza risposta");
        if(!errori.isEmpty()){
            resoconto = new StringBuilder("Elenco degli errori:\n");
            for (Map.Entry<String, String> entry : errori.entrySet()) {
                resoconto.append("- Domanda: ").append(entry.getKey())
                        .append("\n  Errore: ").append(entry.getValue())
                        .append("\n");
            }
        }
        return resoconto.toString();
    }
}
