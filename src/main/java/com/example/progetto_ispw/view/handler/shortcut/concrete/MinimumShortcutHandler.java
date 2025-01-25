package com.example.progetto_ispw.view.handler.shortcut.concrete;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import com.example.progetto_ispw.view.handler.shortcut.interfaces.LogoutShortcutHandler;

//----CLASSE ASTRATTA PER DEFINIRE UNA BASE PER LA CREAZIONE DI CONTROLLER GRAFICI CHE HANNO SOLO LA SHORTCUT PER IL LOGOUT----
public abstract class MinimumShortcutHandler extends PageManager implements LogoutShortcutHandler {
    @Override
    public void logout(){
        try {
            pageLoader.loadPage(PageID.LOGIN);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.PAGE_NOT_FOUND);
        }
    }
}
