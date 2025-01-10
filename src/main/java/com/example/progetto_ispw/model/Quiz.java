package com.example.progetto_ispw.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String titolo;
    private String difficolta;
    private String argomenti;
    private int punteggio = 0;
    private int durata;
    private List<Quesito> quesiti;

    // Costruttore realizzato per mantenere la relazione di composizione
    public Quiz(String titolo, String difficolta, String argomenti, List<Integer> punteggio, int durata, List<String> domande, List<List<String>> risposte, List<List<Boolean>> corrette){
        this.argomenti = argomenti;
        this.durata = durata;
        this.titolo = titolo;
        this.difficolta = difficolta;
        this.quesiti = new ArrayList<>();
        for (int i=0; i<domande.size(); i++){
            this.quesiti.add(new Quesito(domande.get(i),risposte.get(i),corrette.get(i),punteggio.get(i)));
            this.punteggio+=punteggio.get(i);
        }
    }
    // Metodo per rispondere ad un quesito
    public void answering(int quesito, int risposta ){
        quesiti.get(quesito).tickingAnswer(risposta);
    }
    //----METODI SET----
    public void setQuesiti(List<Quesito> quesiti) {
        this.quesiti = quesiti;
    }
    public void setDurata(int durata) {
        this.durata = durata;
    }
    public void setArgomenti(String argomenti) {
        this.argomenti = argomenti;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }
    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }
    //----METODI GET----
    public List<Quesito> getQuesiti() {
        return quesiti;
    }
    public int getDurata() {
        return durata;
    }
    public int getPunteggio() {
        return punteggio;
    }
    public String getArgomenti() {
        return argomenti;
    }
    public String getDifficolta() {
        return difficolta;
    }
    public String getTitolo() {
        return titolo;
    }
}
