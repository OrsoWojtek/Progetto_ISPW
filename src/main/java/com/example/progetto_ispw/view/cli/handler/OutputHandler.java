package com.example.progetto_ispw.view.cli.handler;

//----UTILITY CLASS PER IL METODO DI STAMPA DA USARE NELLA CLI----
public class OutputHandler {
    protected OutputHandler(){
        //Costruttore protected per evitare instaziamento della classe
    }
    //----METOD PER LA STAMPA TRAMITE SYSTEM.OUT----
    public static void showln(String string){
        System.out.println(string);
    }
    public static void show(String string){
        System.out.print(string);
    }
}
