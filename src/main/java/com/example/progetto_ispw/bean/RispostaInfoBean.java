package com.example.progetto_ispw.bean;

public class RispostaInfoBean {
    //----ATTRIBUTI----
    private String testo;
    private boolean corretta; // Attributo che indica se la risposta è corretta
    private boolean ticked; // Attributo per indicare che la risposta è stata selezionata dall'utente
    //----METODI----
    public String getTesto() {
        return testo;
    }
    public boolean isCorretta() {
        return corretta;
    }
    public boolean isTicked(){
        return ticked;
    }
    public void setTesto(String testo) {
        this.testo = testo;
    }
    public void setCorretta(boolean corretta) {
        this.corretta = corretta;
    }
    public void setTicked(boolean ticked) { this.ticked = ticked;}
}
