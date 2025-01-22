package com.example.progetto_ispw.constants;
//----CLASSE ENUM CHE RACCOGLIE UN INSIEME DI IDENTIFICATORI UNIVOCI PER DIVERSE SESSIONI----
public enum SessionID {
    //Costanti definite nella enum, ognuna rappresenta un identificatore unico.
    LOGIN("login"),                 //Identificatore per la sessione di login
    COURSE_PAGE("course_page"),     //Identificatore per la sessione relativa alla pagina di un singolo corso
    CATALOGO_CORSI("catalogo_corsi"), //Identificatore per la sessione contenente il catalogo dei corsi
    QUIZ_PAGE("quiz_page"),         //Identificatore per la sessione relativa alla pagina di un singolo quiz
    CATALOGO_QUIZ("catalogo_quiz"); //Identificatoreper la sessione contenente il catalogo dei quiz
    private final String value;
    SessionID(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
