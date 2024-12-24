package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoFX extends PageManager {
    @FXML
    private Label nomeCorso; //Nome del corso
    private static final String MAINTENANCE = "Funzionalità in manutenzione";
    private  CorsoPageController controller; //Riferimento al controller applicativo associato
    @FXML
    public void initialize(){
        controller = new CorsoPageController();
        nomeCorso.setText(controller.getInfoCourse().getNome()+":"); //Mostra nella pagina del corso nome del corso
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            controller.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),"Errore connessione");
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        controller.clearInfoCourse();
        try{
            pageLoader.loadPage("home");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        }
    }
    @FXML
    public void onSollecitaDomandaClicked(){
        showMessageHandler.showError("Al momento non è possibile sollecitare domande al tutor.\nCi dispiace per il disagio.", MAINTENANCE);
    }
    @FXML
    public void onDescrizioneClicked(){
        showMessageHandler.showMessage(controller.getInfoCourse().getDescrizione(), "Descrizione del corso");
    }
    @FXML
    public void onTeoriaClicked(){
        showMessageHandler.showError("La teoria del corso non è al momento consultabile.\nCi dispiace per il disagio.", MAINTENANCE);
    }
}
