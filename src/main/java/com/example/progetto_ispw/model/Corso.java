package com.example.progetto_ispw.model;

public class Corso implements Entity{
    private String nome;
    private String descrizione;
    public Corso(String nome, String descrizione){
        this.descrizione = descrizione;
        this.nome = nome;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public String getNome() {
        return this.nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
