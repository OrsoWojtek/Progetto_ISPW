package com.example.progetto_ispw.bean;

import java.util.List;

public class QuizInfoBean {
    //----ATTRIBUTI----
    private String titolo;
    private String difficolta;
    private String argomenti;
    private int durata;
    private List<QuesitoInfoBean> quesiti;
    private int punteggio;
    //----METODI----
    public String getTitolo() {
        return titolo;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public String getDifficolta() {
        return difficolta;
    }
    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }
    public String getArgomenti() {
        return argomenti;
    }
    public void setArgomenti(String argomenti) {
        this.argomenti = argomenti;
    }
    public List<QuesitoInfoBean> getQuesiti() {
        return quesiti;
    }
    public void setQuesiti(List<QuesitoInfoBean> quesiti) {
        this.quesiti = quesiti;
    }
    public int getPunteggio() {
        return punteggio;
    }
    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }
    public int getDurata() {
        return durata;
    }
    public void setDurata(int durata) {
        this.durata = durata;
    }
}
