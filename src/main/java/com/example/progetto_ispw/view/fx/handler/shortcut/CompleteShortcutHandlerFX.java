package com.example.progetto_ispw.view.fx.handler.shortcut;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.handler.shortcut.CourseShortcutHandler;

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
