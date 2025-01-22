package com.example.progetto_ispw.view;

import com.example.progetto_ispw.constants.ErrorCode;

public interface ShowMessageHandler { //Interfaccia per modellare le classi che si occupano del display dei messaggi e delle richieste di conferma all'utente
    void showError(String message, ErrorCode code);
    void showMessage(String message, String title);
    boolean askConfirmation(String header, String content);
}
