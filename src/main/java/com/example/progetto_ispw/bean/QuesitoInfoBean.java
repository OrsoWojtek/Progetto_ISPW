package com.example.progetto_ispw.bean;

import java.util.ArrayList;
import java.util.List;

public class QuesitoInfoBean implements Bean{
    //----ATTRIBUTI----
    private String domandaBean;
    private int puntiBean;
    private List<RispostaInfoBean> risposteBean = new ArrayList<>();
    //----METODI----
    public void setPunti(int puntiBean) {
        this.puntiBean = puntiBean;
    }
    public int getPunti() {
        return puntiBean;
    }
    public void setRisposte(List<RispostaInfoBean> risposteBean) {
        this.risposteBean.addAll(risposteBean);
    }
    public List<RispostaInfoBean> getRisposte() {
        return risposteBean;
    }
    public String getDomanda() {
        return domandaBean;
    }
    public void setDomanda(String domandaBean) {
        this.domandaBean = domandaBean;
    }
}
