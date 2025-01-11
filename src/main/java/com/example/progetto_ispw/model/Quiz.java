package com.example.progetto_ispw.model;

import com.example.progetto_ispw.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;

// Implementato il Builder pattern per non avere troppi paramentri nel costruttore del Quiz
public class Quiz {
    private String titolo;
    private String difficolta;
    private String argomenti;
    private int durata;
    private List<Quesito> quesiti;
    private int punteggio;

    // Costruttore privato
    private Quiz(Builder builder) {
        this.titolo = builder.titolo;
        this.difficolta = builder.difficolta;
        this.argomenti = builder.argomenti;
        this.durata = builder.durata;
        this.quesiti = new ArrayList<>(builder.quesiti);
        this.punteggio = builder.quesiti.stream().mapToInt(Quesito::getPunti).sum();
    }
    // Metodo per rispondere ad un quesito
    public void answering(int quesito, int risposta ){
        quesiti.get(quesito).tickingAnswer(risposta);
    }
    // Classe Builder interna
    public static class Builder {
        private String titolo;
        private String difficolta;
        private String argomenti;
        private int durata;
        private List<Quesito> quesiti = new ArrayList<>();

        public Builder setTitolo(String titolo) {
            this.titolo = titolo;
            return this;
        }

        public Builder setDifficolta(String difficolta) {
            this.difficolta = difficolta;
            return this;
        }

        public Builder setArgomenti(String argomenti) {
            this.argomenti = argomenti;
            return this;
        }

        public Builder setDurata(int durata) {
            this.durata = durata;
            return this;
        }

        public Builder addQuesito(String domanda, List<String> risposte, List<Boolean> corrette, int punteggio) {
            this.quesiti.add(new Quesito(domanda, risposte, corrette, punteggio));
            return this;
        }

        public Quiz build() throws DataNotFoundException{
            if (titolo == null || difficolta == null || argomenti == null || quesiti.isEmpty()) {
                throw new DataNotFoundException("Mancano dati obbligatori per costruire il quiz.");
            }
            return new Quiz(this);
        }
    }

    // Getters per gli attributi
    public String getTitolo() {
        return titolo;
    }

    public String getDifficolta() {
        return difficolta;
    }

    public String getArgomenti() {
        return argomenti;
    }

    public int getDurata() {
        return durata;
    }

    public List<Quesito> getQuesiti() {
        return new ArrayList<>(quesiti); // Copia immutabile
    }

    public int getPunteggio() {
        return punteggio;
    }
}
