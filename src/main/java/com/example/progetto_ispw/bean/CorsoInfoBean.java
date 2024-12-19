package com.example.progetto_ispw.bean;

public class CorsoInfoBean {
    //----ATTRIBUTI----
    private String nome;
    private String descrizione;
    //----METODI----
    public CorsoInfoBean(String nome, String descrizione){
        this.nome = nome;
        this.descrizione = descrizione;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public String getDescrizione() {return descrizione;}
    public String getNome() {return nome;}
}
