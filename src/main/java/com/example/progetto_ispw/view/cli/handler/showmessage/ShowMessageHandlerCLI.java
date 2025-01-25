package com.example.progetto_ispw.view.cli.handler.showmessage;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.view.handler.showmessage.ShowMessageHandler;
import java.util.Scanner;

//----CLASSE PER GESTIRE I MESSAGGI DI ERRORE DA MOSTRARE A SCHERMO (VIEW: CLI)
public class ShowMessageHandlerCLI implements ShowMessageHandler {
    private final Scanner scanner = new Scanner(System.in);
    private static final String DIVISORE_ESTERNO = "=================================";
    private static final String DIVISORE_INTERNO = "---------------------------------";
    @Override
    public void showError(String message, ErrorCode code) {
        System.out.println(DIVISORE_ESTERNO);
        System.out.println("❌ ERROR: " + code.getMessage());
        System.out.println(DIVISORE_INTERNO);
        System.out.println(message);
        System.out.println(DIVISORE_ESTERNO);
    }

    @Override
    public void showMessage(String message, String title) {
        System.out.println(DIVISORE_ESTERNO);
        System.out.println("ℹ️ " + title);
        System.out.println(DIVISORE_INTERNO);
        System.out.println(message);
        System.out.println(DIVISORE_ESTERNO);
    }

    @Override
    public boolean askConfirmation(String header, String content) {
        System.out.println(DIVISORE_ESTERNO);
        System.out.println("❓ " + header);
        System.out.println(DIVISORE_INTERNO);
        System.out.println(content);
        System.out.println(DIVISORE_INTERNO);
        System.out.print("Confermi? (s/n): ");

        //Leggi l'input dell'utente e valuta la conferma
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("s") || input.equals("si") || input.equals("yes");
    }
}
