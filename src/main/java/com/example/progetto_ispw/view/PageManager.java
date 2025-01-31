package com.example.progetto_ispw.view;

import com.example.progetto_ispw.view.handler.pageloader.PageLoader;
import com.example.progetto_ispw.view.handler.showmessage.ShowMessageHandler;

//----CLASSE PADRE PER LA GESTIONE DI UNA 'PAGINA' DELLA VIEW DEL PROGETTO----
public abstract class PageManager {
    protected PageLoader pageLoader; //ATTRIBUTO PER LA GESTIONE DEL CARICAMENTO DELLE PAGINE
    protected ShowMessageHandler showMessageHandler; //ATTRIBUTO PER LA GESTIONE DEI MESSAGGI DI ERRORE
    public void setupDependencies(PageLoader pageLoader, ShowMessageHandler showMessageHandler){ //METODO PER L'INIZIALIZZAZIONE
        this.pageLoader = pageLoader;
        this.showMessageHandler = showMessageHandler;
    }
    public abstract void initialize();
}
