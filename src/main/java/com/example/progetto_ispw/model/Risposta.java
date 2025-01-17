package com.example.progetto_ispw.model;

public class Risposta implements Entity{
    private String testo;
    private boolean corretta; // Attributo che indica se la risposta è corretta
    private boolean ticked; // Attributo per indicare che la risposta è stata selezionata dall'utente

    public Risposta(String testo, boolean corretta){
        this.corretta = corretta;
        this.testo = testo;
        this.ticked = false;
    }
    // Metodo per spuntare una risposta
    public void selected(){
        this.ticked = true;
    }
    //----METODI GET----
    public String getTesto() {
        return testo;
    }
    public boolean isCorretta() {
        return corretta;
    }
    public boolean isTicked(){
        return ticked;
    }
    //----METODI SET----
    public void setTesto(String testo) {
        this.testo = testo;
    }
    public void setCorretta(boolean corretta) {
        this.corretta = corretta;
    }
}
