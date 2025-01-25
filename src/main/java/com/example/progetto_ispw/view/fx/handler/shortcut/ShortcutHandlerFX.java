package com.example.progetto_ispw.view.fx.handler.shortcut;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.handler.shortcut.ShortcutHandler;

//----CLASSE ASTRATTA PER DEFINIRE UNA BASE PER LA CREAZIONE DI CONTROLLER GRAFICI CHE HANNO LE SHORTCUT PER IL LOGOUT E HOME (VERSIONE FX)----
public abstract class ShortcutHandlerFX extends PageManager implements ShortcutHandler {
    @Override
    public void logout(){
        try {
            pageLoader.loadPage(PageID.LOGIN);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.PAGE_NOT_FOUND);
        }
    }
    @Override
    public void home(){
        try{
            pageLoader.loadPage(PageID.HOME);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        }
    }
}
