package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.model.Sessione;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoFX extends PageManager {
    @FXML
    private Label nomeCorso; //Nome del corso
    @FXML
    public void initialize(){
        nomeCorso.setText(Sessione.getCourse().getNome()+"\n"+Sessione.getCourse().getDescrizione()); //Mostra nella pagina del corso nome e descrizione del corso
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        CorsoPageController corsoPageController = new CorsoPageController();
        try {
            corsoPageController.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showErrorHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showErrorHandler.showError(e.getMessage(),"Errore connessione");
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        CorsoPageController corsoPageController = new CorsoPageController();
        corsoPageController.clearInfoCourse();
        try{
            pageLoader.loadPage("home");
        } catch (PageNotFoundException e){
            showErrorHandler.showError(e.getMessage(),"Pagina non trovata");
        }
    }
}
