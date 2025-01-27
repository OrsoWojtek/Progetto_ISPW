package com.example.progetto_ispw.bean;

import java.util.ArrayList;
import java.util.List;

public class QuizInfoBean implements Bean{
    //----ATTRIBUTI----
    private String titoloBean;
    private String difficoltaBean;
    private String argomentiBean;
    private int durataBean;
    private List<QuesitoInfoBean> quesitiBean = new ArrayList<>();
    private int numeroDomandeBean;
    private int punteggioBean; //PUNTEGGIO TOTALE DEL QUIZ
    private int punteggioStudente = 0; //PUNTEGGIO OTTENUTO DALLO STUDENTE
    private int tempo; //Tempo impiegato per completare il quiz
    //----METODI----
    public String getTitolo() {
        return titoloBean;
    }
    public void setTitolo(String titoloBean) {
        this.titoloBean = titoloBean;
    }
    public String getDifficolta() {
        return difficoltaBean;
    }
    public void setDifficolta(String difficoltaBean) { this.difficoltaBean = difficoltaBean;}
    public String getArgomenti() {
        return argomentiBean;
    }
    public void setArgomenti(String argomentiBean) {
        this.argomentiBean = argomentiBean;
    }
    public List<QuesitoInfoBean> getQuesiti() {
        return quesitiBean;
    }
    public void setQuesiti(List<QuesitoInfoBean> quesitiBean) {
        this.quesitiBean.addAll(quesitiBean);
    }
    public int getPunteggio() {
        return punteggioBean;
    }
    public void setPunteggio(int punteggioBean) {
        this.punteggioBean = punteggioBean;
    }
    public int getDurata() {
        return durataBean;
    }
    public void setDurata(int durataBean) {
        this.durataBean = durataBean;
    }
    public int getNumeroDomandeBean() {
        return numeroDomandeBean;
    }
    public void setNumeroDomandeBean() {
        this.numeroDomandeBean = this.quesitiBean.size();
    }
    public void setPunteggioStudente(int punti){
        this.punteggioStudente+=punti;
    }
    public int getPunteggioStudente(){return punteggioStudente;}
    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
    public String showTempo() {
        String tempoRim;
        int secondi= tempo%60;
        int minuti= tempo/60;
        if(minuti < 10){
            tempoRim = "0" + minuti + ":";
        } else {
            tempoRim = minuti + ":";
        }
        if(secondi < 10){
            tempoRim += "0" + secondi;
        } else {
            tempoRim += "" + secondi;
        }
        return tempoRim;
    }
}
