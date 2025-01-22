package com.example.progetto_ispw.constants;
//----CLASSE ENUM CHE RAPPRESENTA GLI IDENTIFICATORI DELLE PAGINE DEL PROGETTO----
public enum PageID {
    LOGIN("login"),
    HOME("home"),
    CORSO("corso"),
    QUIZ("quiz"),
    SVOLGIMENTO_QUIZ("svolgimento_quiz"),
    ESITO_QUIZ("esito_quiz");

    private final String value;
    PageID(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
