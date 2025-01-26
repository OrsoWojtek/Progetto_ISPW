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
import com.example.progetto_ispw.view.cli.handler.OutputHandler;
import com.example.progetto_ispw.view.handler.shortcut.concrete.CompleteShortcutHandler;

import java.util.*;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: HOME)----
public class HomeCLI extends CompleteShortcutHandler {
    private String userRole; //Ruolo dell'utente loggato
    private List<CorsoInfoBean> catalogo; //Catalogo dei corsi a cui l'utente è iscritto
    private HomeController home; //Controller applicativo
    private int currentPage = 0; //Indice della pagina corrente dei corsi da mostrare
    private static final String NOTVALID = "Scelta non valida. Riprova.";
    private final Scanner scanner = new Scanner(System.in);

    //----METODO PER L'INIZIALIZZAZIONE DELLA GRAFICA----
    @Override
    public void initialize() {
        home = new HomeController();
        try {
            //Recupera informazioni sull'utente corrente
            UtenteInfoBean user = home.getInfoUser();
            String username = user.getUsername();
            this.userRole = user.getRole();

            //Mostra informazioni di benvenuto
            OutputHandler.showln("Benvenuto, " + username + " (" + userRole + ")");

            //Recupera il catalogo dei corsi
            catalogo = home.getCorsiFrequentati(user);
            if ("".equals(catalogo.getFirst().getDescrizione())) {
                OutputHandler.showln("Non sei iscritto a nessun corso.");
            }
            showCourseCatalog();
        } catch (ConnectionException e) {
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            onLogout();
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
        showMessageHandler.showError("Iscrizione ai corsi: "+StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showCourseCatalog();
    }

    //----METODO PER ANDARE ALLA PAGINA DI CREAZIONE DI UN NUOVO CORSO----
    public void onAdd() {
        showMessageHandler.showError("Creazione di un nuovo corso: "+StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showCourseCatalog();
    }

    //----METODO PER GESTIRE I CORSI----
    public void onOption() {
        showMessageHandler.showError("Modifica di un corso: "+StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
        showCourseCatalog();
    }

    //----METODO PER VISUALIZZARE IL CATALOGO DEI CORSI----
    private void showCourseCatalog() {
        int coursesPerPage = 6; //Numero massimo di corsi per pagina
        int startIndex = currentPage * coursesPerPage;
        int endIndex = Math.min(startIndex + coursesPerPage, catalogo.size());
        if(!"".equals(catalogo.getFirst().getDescrizione())) { //Se ci sono corsi alla mostra il catalogo
            OutputHandler.showln("\nCatalogo corsi (Pagina " + (currentPage + 1) + "):\n");
            for (int i = startIndex; i < endIndex; i++) {
                CorsoInfoBean corso = catalogo.get(i);
                OutputHandler.showln((i + 1) + ". " + corso.getNome());
            }
        }
        addNavigationOptions(endIndex);
    }

    //----METODO PER AGGIUNGERE OPZIONI DI NAVIGAZIONE----
    private void addNavigationOptions(int endIndex) {
        OutputHandler.showln("\nOpzioni:");
        if (currentPage > 0) {
            OutputHandler.showln("[P] Pagina precedente");
        }
        if (endIndex < catalogo.size()) {
            OutputHandler.showln("[N] Pagina successiva");
        }
        if (!"".equals(catalogo.getFirst().getDescrizione())) { //Se il catalogo contiene corsi
            OutputHandler.showln("[S] Seleziona corso");  //Mostra l'opzione di selezione
        }
        OutputHandler.showln("[L] Logout");
        if (userRole.equalsIgnoreCase(UserRole.TUTOR.getValue())){
            OutputHandler.showln("[A] Aggiungi un nuovo corso");
            OutputHandler.showln("[M] Modifica un corso");
        }
        if (userRole.equalsIgnoreCase(UserRole.STUDENTE.getValue())){
            OutputHandler.showln("[I] Iscriviti a un nuovo corso");
        }

        handleNavigationInput();
    }

    //----METODO PER GESTIRE L'INPUT DI NAVIGAZIONE----
    private void handleNavigationInput() {
        OutputHandler.show("Inserisci la tua scelta: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        //Mappa dei comandi e delle azioni
        Map<String, Runnable> actions = new HashMap<>();
        actions.put("P", this::handlePreviousPage);
        actions.put("N", this::handleNextPage);
        actions.put("S", this::selectCourse);
        actions.put("L", this::onLogout);
        actions.put("A", () -> handleRoleBasedAction(UserRole.STUDENTE, this::onAdd));
        actions.put("M", () -> handleRoleBasedAction(UserRole.STUDENTE, this::onOption));
        actions.put("I", () -> handleRoleBasedAction(UserRole.TUTOR, this::onSearch));

        //Esegui l'azione corrispondente, o mostra un messaggio per scelta non valida
        actions.getOrDefault(choice, () -> {
            OutputHandler.showln(NOTVALID);
            showCourseCatalog();
        }).run();
    }

    //----METODO PER MOSTRARE LA PAGINA PRECEDENTE DEL CATALOGO DEI CORSI----
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
        } else {
            OutputHandler.showln("Non ci sono pagine precedenti.");
        }
        showCourseCatalog();
    }

    //----METODO PER MOSTRARE LA PAGINA SUCCESSIVA DEL CATALOGO DEI CORSI----
    private void handleNextPage() {
        if ((currentPage + 1) * 6 < catalogo.size()) {
            currentPage++;
        } else {
            OutputHandler.showln("Non ci sono altre pagine.");
        }
        showCourseCatalog();
    }

    //----METODO PER VERIFICARE IL PERMESSO DI USARE UNA CERTA AZIONE IN BASE AL RUOLO DELL'UTENTE----
    private void handleRoleBasedAction(UserRole forbiddenRole, Runnable action) {
        if (userRole.equalsIgnoreCase(forbiddenRole.getValue())) {
            OutputHandler.showln(NOTVALID);
            showCourseCatalog();
        } else {
            action.run();
        }
    }

    //----METODO PER SELEZIONARE UN CORSO----
    private void selectCourse() {
        if(!"".equals(catalogo.getFirst().getDescrizione())) { //Se ci sono dei corsi nel catalogo esegui la selezione
            OutputHandler.show("Inserisci il numero del corso: ");
            try {
                int courseIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (courseIndex >= 0 && courseIndex < catalogo.size()) {
                    goToCoursePage(catalogo.get(courseIndex).getNome());
                } else {
                    OutputHandler.showln("Numero corso non valido.");
                    selectCourse();
                }
            } catch (NumberFormatException e) {
                OutputHandler.showln("Input non valido. Riprova.");
                selectCourse();
            }
        } else {
            OutputHandler.showln(NOTVALID);     //Altrimenti mostra la selezione non valida
            showCourseCatalog();
        }
    }

    //----METODO PER PASSARE ALLA PAGINA DEL CORSO DESIDERATO----
    private void goToCoursePage(String nomeCorso) {
        Optional<CorsoInfoBean> corsoScelto = catalogo.stream().filter(c -> c.getNome().equals(nomeCorso)).findFirst(); //Cerca il bean del corso nel catalogo corrispondere al nome del corso selezionato dall'utente
        corsoScelto.ifPresentOrElse(currentCourse -> {
            try {
                home.setInfoCourse(currentCourse); //Passa il bean del corso al controller applicativo
                course();  //...Mostra la pagina del corso
            } catch (DataNotFoundException e) {
                showMessageHandler.showError(e.getMessage(), ErrorCode.SESSION);
                onLogout();
            } catch (DataSessionCastingException e) {
                showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
                onLogout();
            }
        }, () -> {
                OutputHandler.showln("Corso non trovato.");
                showCourseCatalog();
            });
    }
}
