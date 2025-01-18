package com.example.progetto_ispw.bean;

public class RispostaInfoBean implements Bean{
    //----ATTRIBUTI----
    private String testoBean;
    private boolean correttaBean; // Attributo che indica se la risposta è corretta
    private boolean tickedBean; // Attributo per indicare che la risposta è stata selezionata dall'utente
    //----METODI----
    public String getTesto() {
        return testoBean;
    }
    public boolean isCorretta() {
        return correttaBean;
    }
    public boolean isTicked(){
        return tickedBean;
    }
    public void setTesto(String testoBean) {
        this.testoBean = testoBean;
    }
    public void setCorretta(boolean correttaBean) {
        this.correttaBean = correttaBean;
    }
    public void setTicked(boolean tickedBean) { this.tickedBean = tickedBean;}
}
