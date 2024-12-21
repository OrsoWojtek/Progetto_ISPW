package com.example.progetto_ispw.view;

public interface PageManagerAware { //Interfaccia per modellare i controller grafici in modo da poter essere a conoscenza della loro relativa PageLoader
    void setPageManager(PageLoader pageLoader);
}
