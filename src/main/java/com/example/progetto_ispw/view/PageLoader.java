package com.example.progetto_ispw.view;

public interface PageLoader { //Interfaccia per modellare le classi che si occupano del caricamento delle pagine e dei messaggi di errore
    void loadPage(String page);
    void showErrorPopup(String message, String title);
}
