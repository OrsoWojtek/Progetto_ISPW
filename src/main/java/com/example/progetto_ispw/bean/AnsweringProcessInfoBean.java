package com.example.progetto_ispw.bean;

public class AnsweringProcessInfoBean implements Bean{
    //----ATTRIBUTI----
    private QuesitoInfoBean quesitoInfoBean;
    private String risposta;
    //----METODI----
    public QuesitoInfoBean getQuesitoInfoBean() {
        return quesitoInfoBean;
    }
    public void setQuesitoInfoBean(QuesitoInfoBean quesitoInfoBean) {
        this.quesitoInfoBean = quesitoInfoBean;
    }
    public String getRisposta() {
        return risposta;
    }
    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }
}
