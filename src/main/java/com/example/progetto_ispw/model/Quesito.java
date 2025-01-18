package com.example.progetto_ispw.model;

import java.util.ArrayList;
import java.util.List;
public class Quesito {
    private final String domanda;
    private final int punti;
    private List<Risposta> risposte;

    // Costruttore privato per forzare l'uso del Builder
    private Quesito(Builder builder) {
        this.domanda = builder.domanda;
        this.punti = builder.punti;
        this.risposte = builder.risposte;
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

    //----METODI UTILI----
    // Metodo per spuntare la risposta scelta dall'utente
    public void tickingAnswer(int index) {
        this.risposte.get(index).selected();
    }

    //----BUILDER CLASS----
    public static class Builder {
        private String domanda;
        private int punti;
        private List<Risposta> risposte = new ArrayList<>();

        // Metodo per impostare la domanda
        public Builder setDomanda(String domanda) {
            this.domanda = domanda;
            return this;
        }

        // Metodo per impostare i punti
        public Builder setPunti(int punti) {
            this.punti = punti;
            return this;
        }

        // Metodo per aggiungere una singola risposta
        public void addRisposta(String testo, boolean corretta) {
            this.risposte.add(new Risposta(testo, corretta));
        }

        // Metodo per costruire l'oggetto Quesito
        public Quesito build() {
            return new Quesito(this);
        }
    }
}
