package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoFX extends PageManager {
    @FXML
    private Label nomeCorso; //Nome del corso
    @FXML
    private Text sollecitaDomanda; //Testo per la richiesta di "sollecita domanda"
    @FXML
    private Text visualizzaDomande; //Testo per la richiesta di "visualizza domande"
    private static final String MAINTENANCE = "Funzionalità in manutenzione";
    private  CorsoPageController controller; //Riferimento al controller applicativo associato
    @FXML
    public void initialize(){
        controller = new CorsoPageController();
        UtenteInfoBean user = controller.getInfoUser();
        nomeCorso.setText(controller.getInfoCourse().getNome()+":"); //Mostra nella pagina del corso il nome del corso
        visualizzaDomande.setVisible("tutor".equalsIgnoreCase(user.getRole())); //Mostra il testo per accedere alla funzionalità disponibile solo per i tutor
        sollecitaDomanda.setVisible("studente".equalsIgnoreCase(user.getRole())); //Mostra il testo per accedere alla funzionalità disponibile solo per gli studenti
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
    public void onMessageBoxClicked(){
        if(sollecitaDomanda.isVisible()){
            onSollecitaDomandaClicked();
        } else {
            onVisualizzaDomandeClicked();
        }
    }
    @FXML
    public void onSollecitaDomandaClicked(){
        showMessageHandler.showError("Al momento non è possibile sollecitare domande al tutor.\nCi dispiace per il disagio.", MAINTENANCE);
    }
    @FXML
    public void onVisualizzaDomandeClicked(){
        showMessageHandler.showError("Al momento non è possibile visualizzare domande in arrivo dagli studenti.\nCi dispiace per il disagio.", MAINTENANCE);
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
