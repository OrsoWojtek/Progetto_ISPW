package com.example.progetto_ispw.bean;

public class CorsoInfoBean implements Bean{
    //----ATTRIBUTI----
    private String nome;
    private String descrizione;
    //----METODI----
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public String getDescrizione() {return descrizione;}
    public String getNome() {return nome;}
}
