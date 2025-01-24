package com.example.progetto_ispw.view.fx.handler.shortcut;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.handler.shortcut.CourseShortcutHandler;

//----CLASSE ASTRATTA PER DEFINIRE UNA BASE PER LA CREAZIONE DI CONTROLLER GRAFICI CHE HANNO TUTTE LE SHORTCUT AL MOMENTO DISPONIBILI  (LOGOUT, HOME E CORSO)----
public abstract class CompleteShortcutHandlerFX extends ShortcutHandlerFX implements CourseShortcutHandler {
    @Override
    public void course() {
        try{
            pageLoader.loadPage(PageID.CORSO);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.PAGE_NOT_FOUND);
        }
    }
}
