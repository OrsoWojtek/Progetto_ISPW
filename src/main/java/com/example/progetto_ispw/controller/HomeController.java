package com.example.progetto_ispw.controller;

import java.util.List;

//----CONTROLLER APPLICATIVO PER GESTIRE LA HOME----
public class HomeController {
    //----METODO PER RICERCARE QUALI SONO I CORSI A CUI L'UTENTE È ISCRITTO----
    public List<String> getCorsiFrequentati(){
        List<String> courses = List.of("Analisi 1","Basi di Dati","ISPW");
        return courses;
    }
}
