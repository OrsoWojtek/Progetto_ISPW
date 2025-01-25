package com.example.progetto_ispw.view.cli.handler.pageloader;

import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.cli.*;
import com.example.progetto_ispw.view.cli.handler.showmessage.ShowMessageHandlerCLI;
import com.example.progetto_ispw.view.handler.pageloader.PageLoader;

//----CLASSE PER ESEGUIRE LE OPERAZIONI RELATIVE AL CARICAMENTO DELLE SUCCESSIVE PAGINE IN CLI
public class PageLoaderCLI implements PageLoader {
    //----METODO PER EFFETTUARE IL CAMBIO PAGINA----
    @Override
    public void loadPage(PageID pageID) throws PageNotFoundException {
        PageManager controller;
        switch (pageID){
            case HOME ->{
                controller = new HomeCLI();
                controller.setupDependencies(new PageLoaderCLI(),new ShowMessageHandlerCLI());
                controller.initialize();
            }
            case CORSO -> {
                controller = new CorsoCLI();
                controller.setupDependencies(new PageLoaderCLI(),new ShowMessageHandlerCLI());
                controller.initialize();
            }
            case QUIZ -> {
                controller = new QuizCLI();
                controller.setupDependencies(new PageLoaderCLI(),new ShowMessageHandlerCLI());
                controller.initialize();
            }
            case LOGIN -> {
                controller = new LoginCLI();
                controller.setupDependencies(new PageLoaderCLI(),new ShowMessageHandlerCLI());
                controller.initialize();
            }
            case ESITO_QUIZ -> {
                controller = new EsitoQuizCLI();
                controller.setupDependencies(new PageLoaderCLI(),new ShowMessageHandlerCLI());
                controller.initialize();
            }
            case SVOLGIMENTO_QUIZ ->{
                controller = new SvolgiQuizCLI();
                controller.setupDependencies(new PageLoaderCLI(),new ShowMessageHandlerCLI());
                controller.initialize();
            }
            default -> throw new PageNotFoundException("Errore nell'apertura della pagina di "+pageID.getValue().toLowerCase());
        }
    }
}
