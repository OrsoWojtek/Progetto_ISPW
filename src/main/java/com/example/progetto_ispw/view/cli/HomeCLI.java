package com.example.progetto_ispw.view.cli;
import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.constants.UserRole;
import com.example.progetto_ispw.controller.HomeController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataSessionCastingException;
import com.example.progetto_ispw.view.handler.shortcut.concrete.CompleteShortcutHandler;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: HOME)----
public class HomeCLI extends CompleteShortcutHandler {
    private String userRole; //Ruolo dell'utente loggato
    private List<CorsoInfoBean> catalogo; //Catalogo dei corsi a cui l'utente è iscritto
    private HomeController home; //Controller applicativo
    private int currentPage = 0; //Indice della pagina corrente dei corsi da mostrare
    private final Scanner scanner = new Scanner(System.in);

    //----Inizializzazione della pagina Home ----
    @Override
    public void initialize() {
        home = new HomeController();
        try {
            //Recupera informazioni sull'utente corrente
            UtenteInfoBean user = home.getInfoUser();
            String username = user.getUsername();
            this.userRole = user.getRole();

            //Mostra informazioni di benvenuto
            System.out.println("Benvenuto, " + username + " (" + userRole + ")");

            //Recupera il catalogo dei corsi
            catalogo = home.getCorsiFrequentati(user);
            if ("".equals(catalogo.getFirst().getDescrizione())) {
                System.out.println("Non sei iscritto a nessun corso.");
            } else {
                showCourseCatalog();
            }
        } catch (ConnectionException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            System.exit(1);
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
            onLogout();
        } catch (DataSessionCastingException e) {
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
            onLogout();
        }
    }

    //----METODO PER EFFETTUARE IL LOGOUT----
    public void onLogout() {
        try {
            home.clean();
            logout();
        } catch (ConnectionException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            System.exit(1);
        }
    }

    //----METODO PER ISCRIVERSI AI CORSI----
    public void onSearch() {
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showCourseCatalog();
    }

    //----METODO PER ANDARE ALLA PAGINA DI CREAZIONE DI UN NUOVO CORSO----
    public void onAdd() {
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showCourseCatalog();
    }

    //----METODO PER GESTIRE I CORSI----
    public void onOption() {
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showCourseCatalog();
    }

    //----METODO PER VISUALIZZARE IL CATALOGO DEI CORSI----
    private void showCourseCatalog() {
        int coursesPerPage = 6; //Numero massimo di corsi per pagina
        int startIndex = currentPage * coursesPerPage;
        int endIndex = Math.min(startIndex + coursesPerPage, catalogo.size());

        System.out.println("\nCatalogo corsi (Pagina " + (currentPage + 1) + "):\n");
        for (int i = startIndex; i < endIndex; i++) {
            CorsoInfoBean corso = catalogo.get(i);
            System.out.println((i + 1) + ". " + corso.getNome());
        }

        addNavigationOptions(endIndex);
    }

    //----METODO PER AGGIUNGERE OPZIONI DI NAVIGAZIONE----
    private void addNavigationOptions(int endIndex) {
        System.out.println("\nOpzioni:");
        if (currentPage > 0) {
            System.out.println("[P] Pagina precedente");
        }
        if (endIndex < catalogo.size()) {
            System.out.println("[N] Pagina successiva");
        }
        System.out.println("[S] Seleziona corso");
        System.out.println("[L] Logout");
        if (userRole.equalsIgnoreCase(UserRole.TUTOR.getValue())){
            System.out.println("[A] Aggiungi un nuovo corso");
            System.out.println("[M] Modifica un corso");
        }
        if (userRole.equalsIgnoreCase(UserRole.STUDENTE.getValue())){
            System.out.println("[I] Iscriviti a un nuovo corso");
        }

        handleNavigationInput();
    }

    //----METODO PER GESTIRE L'INPUT DI NAVIGAZIONE----
    private void handleNavigationInput() {
        System.out.print("Inserisci la tua scelta: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        switch (choice) {
            case "P":
                if (currentPage > 0) {
                    currentPage--;
                    //showCourseCatalog();
                } else {
                    System.out.println("Non ci sono pagine precedenti.");
                }
                showCourseCatalog();
                break;
            case "N":
                if ((currentPage + 1) * 6 < catalogo.size()) {
                    currentPage++;
                    //showCourseCatalog();
                } else {
                    System.out.println("Non ci sono altre pagine.");
                }
                showCourseCatalog();
                break;
            case "S":
                selectCourse();
                break;
            case "L":
                onLogout();
                break;
            case "A":
                if(userRole.equalsIgnoreCase(UserRole.STUDENTE.getValue())){
                    System.out.println("Scelta non valida. Riprova.");
                    handleNavigationInput();
                } else {
                    onAdd();
                    break;
                }
            case "M":
                if(userRole.equalsIgnoreCase(UserRole.STUDENTE.getValue())){
                    System.out.println("Scelta non valida. Riprova.");
                    handleNavigationInput();
                } else {
                    onOption();
                    break;
                }
            case "I":
                if(userRole.equalsIgnoreCase(UserRole.TUTOR.getValue())){
                    System.out.println("Scelta non valida. Riprova.");
                    handleNavigationInput();
                } else {
                    onSearch();
                    break;
                }
            default:
                System.out.println("Scelta non valida. Riprova.");
                handleNavigationInput();
        }
    }

    //----METODO PER SELEZIONARE UN CORSO----
    private void selectCourse() {
        System.out.print("Inserisci il numero del corso: ");
        try {
            int courseIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (courseIndex >= 0 && courseIndex < catalogo.size()) {
                goToCoursePage(catalogo.get(courseIndex).getNome());
            } else {
                System.out.println("Numero corso non valido.");
                selectCourse();
            }
        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Riprova.");
            selectCourse();
        }
    }

    //----METODO PER PASSARE ALLA PAGINA DEL CORSO DESIDERATO----
    private void goToCoursePage(String nomeCorso) {
        Optional<CorsoInfoBean> corsoScelto = catalogo.stream().filter(c -> c.getNome().equals(nomeCorso)).findFirst();
        corsoScelto.ifPresentOrElse(currentCourse -> {
            try {
                home.setInfoCourse(currentCourse);
                System.out.println("Sei entrato nel corso: " + nomeCorso);
            } catch (DataNotFoundException e) {
                showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
                onLogout();
            } catch (DataSessionCastingException e) {
                showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
                onLogout();
            }
        }, () -> {
                System.out.println("Corso non trovato.");
                showCourseCatalog();
            });
    }
}
