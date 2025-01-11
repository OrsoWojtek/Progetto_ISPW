package com.example.progetto_ispw.bean;

import java.util.List;

public class QuizInfoBean {
    //----ATTRIBUTI----
    private String titoloBean;
    private String difficoltaBean;
    private String argomentiBean;
    private int durataBean;
    private List<QuesitoInfoBean> quesitiBean;
    private int punteggioBean;
    //----METODI----
    public String getTitolo() {
        return titoloBean;
    }
    public void setTitolo(String titolo) {
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
        this.quesitiBean = quesitiBean;
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
}
