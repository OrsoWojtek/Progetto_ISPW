package com.example.progetto_ispw.view;

import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;

public interface PageLoader { //Interfaccia per modellare le classi che si occupano del caricamento delle pagine
    void loadPage(PageID pageID) throws PageNotFoundException;
}
