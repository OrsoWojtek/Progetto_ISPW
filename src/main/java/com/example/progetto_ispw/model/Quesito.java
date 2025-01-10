package com.example.progetto_ispw.model;

import java.util.ArrayList;
import java.util.List;

public class Quesito {
    private String domanda;
    private int punti;
    private List<Risposta> risposte;
    // Costruttore realizzato per mantenere la relazione di composizione
    public Quesito(String domanda, List<String> risposte, List<Boolean> corrette, int punti){
        this.risposte = new ArrayList<>();
        for(int i=0; i<risposte.size(); i++){
            this.risposte.add(new Risposta(risposte.get(i),corrette.get(i)));
        }
        this.domanda = domanda;
        this.punti = punti;
    }
    // Metodo per spuntare la risposta scelta dall'utente
    public void tickingAnswer(int index){
        this.risposte.get(index).selected();
    }
    //----METODI SET----
    public void setPunti(int punti) {
        this.punti = punti;
    }
    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }
    public void setRisposte(List<Risposta> risposte) {
        this.risposte = risposte;
    }
    //----METODI GET----
    public int getPunti() {
        return punti;
    }
    public String getDomanda() {
        return domanda;
    }
    public List<Risposta> getRisposte() {
        return risposte;
    }
}
