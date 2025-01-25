package com.example.progetto_ispw.view.handler.shortcut.concrete;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.handler.shortcut.interfaces.HomeShortcutHandler;

//----CLASSE ASTRATTA PER DEFINIRE UNA BASE PER LA CREAZIONE DI CONTROLLER GRAFICI CHE HANNO LE SHORTCUT PER IL LOGOUT E HOME----
public abstract class BaseShortcutHandler extends MinimumShortcutHandler implements HomeShortcutHandler{
    @Override
    public void home(){
        try{
            pageLoader.loadPage(PageID.HOME);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        }
    }
}
