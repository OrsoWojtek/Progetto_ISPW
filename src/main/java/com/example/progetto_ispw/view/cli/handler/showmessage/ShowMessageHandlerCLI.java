package com.example.progetto_ispw.view.cli.handler.showmessage;

import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.view.cli.handler.OutputHandler;
import com.example.progetto_ispw.view.handler.showmessage.ShowMessageHandler;
import java.util.Scanner;

//----CLASSE PER GESTIRE I MESSAGGI DI ERRORE DA MOSTRARE A SCHERMO (VIEW: CLI)
public class ShowMessageHandlerCLI implements ShowMessageHandler {
    private final Scanner scanner = new Scanner(System.in);
    private static final String DIVISORE_ESTERNO = "=================================";
    private static final String DIVISORE_INTERNO = "---------------------------------";
    @Override
    public void showError(String message, ErrorCode code) {
        OutputHandler.showln(DIVISORE_ESTERNO);
        OutputHandler.showln("❌ ERROR: " + code.getMessage());
        OutputHandler.showln(DIVISORE_INTERNO);
        OutputHandler.showln(message);
        OutputHandler.showln(DIVISORE_ESTERNO);
    }

    @Override
    public void showMessage(String message, String title) {
        OutputHandler.showln(DIVISORE_ESTERNO);
        OutputHandler.showln("ℹ️ " + title);
        OutputHandler.showln(DIVISORE_INTERNO);
        OutputHandler.showln(message);
        OutputHandler.showln(DIVISORE_ESTERNO);
    }

    @Override
    public boolean askConfirmation(String header, String content) {
        OutputHandler.showln(DIVISORE_ESTERNO);
        OutputHandler.showln("❓ " + header);
        OutputHandler.showln(DIVISORE_INTERNO);
        OutputHandler.showln(content);
        OutputHandler.showln(DIVISORE_INTERNO);
        OutputHandler.show("Confermi? (s/n): ");

        //Leggi l'input dell'utente e valuta la conferma
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("s") || input.equals("si") || input.equals("yes");
    }
}
