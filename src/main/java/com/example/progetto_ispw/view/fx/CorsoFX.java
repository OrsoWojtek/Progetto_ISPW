package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.model.Sessione;
import com.example.progetto_ispw.view.PageLoader;
import com.example.progetto_ispw.view.PageManagerAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoFX implements PageManagerAware {
    @FXML
    private Label nomeCorso; //Username dell'utente loggato
    private PageLoader pageManager; //Gestore dello switch di pagina
    @FXML
    public void initialize(){
        nomeCorso.setText(Sessione.getCourse().getNome()+"\n"+Sessione.getCourse().getDescrizione()); //Mostra nella home l'username dell'utente
    }
    @Override
    public void setPageManager(PageLoader pageManager) {
        this.pageManager = pageManager;
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        CorsoPageController corsoPageController = new CorsoPageController();
        corsoPageController.clean();
        pageManager.loadPage("login");
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        CorsoPageController corsoPageController = new CorsoPageController();
        corsoPageController.clearInfoCourse();
        pageManager.loadPage("home");
    }
}
