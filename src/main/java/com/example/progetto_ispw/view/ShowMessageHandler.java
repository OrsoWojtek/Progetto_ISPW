package com.example.progetto_ispw.view;

public interface ShowMessageHandler { //Interfaccia per modellare le classi che si occupano del display dei messaggi
    void showError(String message, String title);
    void showMessage(String message, String title);
}
