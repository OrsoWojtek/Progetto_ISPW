package com.example.progetto_ispw.bean;

import java.util.List;

public class QuesitoInfoBean {
    //----ATTRIBUTI----
    private String domanda;
    private int punti;
    private List<RispostaInfoBean> risposte;
    //----METODI----
    public void setPunti(int punti) {
        this.punti = punti;
    }
    public int getPunti() {
        return punti;
    }
    public void setRisposte(List<RispostaInfoBean> risposte) {
        this.risposte = risposte;
    }
    public List<RispostaInfoBean> getRisposte() {
        return risposte;
    }
    public String getDomanda() {
        return domanda;
    }
    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }
}
