package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.RoleNotFoundException;
import com.example.progetto_ispw.view.PageLoader;
import com.example.progetto_ispw.view.PageManagerAware;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: LOGIN)----
public class LoginFX implements PageManagerAware {
    //----USERNAME UTENTE----
    @FXML
    private TextField username;
    //----PASSWORD UTENTE----
    @FXML
    private PasswordField pssw;
    private PageLoader pageManager;

    //----METODO CHIAMATO AL CLICK DEL TASTO DI LOGIN----
    @FXML
    public void onLoginButtonClick(){
        LoginInfoBean bean = new LoginInfoBean();               //Istanziamento bean per il login
        LoginController controller = new LoginController();     //Istanziamento controller
        bean.setUsername(username.getText());                   //Setting del bean
        bean.setPassword(pssw.getText());
        try {
            controller.checkLogin(bean);     //Se le credenziali inserite sono corrette...
            pageManager.loadPage("home");    //...Mostra la pagina di home
        } catch (DataNotFoundException e){                                            //Altrimenti...
            pageManager.showErrorPopup(e.getMessage(), "Credenziali errate");   //...Mostra un popup di errore
        } catch (DataAccessException e){
            pageManager.showErrorPopup(e.getMessage(),"Errore DB");
        } catch (RoleNotFoundException e){
            pageManager.showErrorPopup(e.getMessage(), "Ruolo indefinito");
        }

    }
    @Override
    public void setPageManager(PageLoader pageManager) {
        this.pageManager = pageManager;
    }
    //----METODO CHIAMATO AL CLICK DELLA RICHIESTA DI REGISTRAZIONE----
    @FXML
    public void onRegistReqClick(){
        pageManager.showErrorPopup("La funzione di registrazione è al momento disabilitata.\nCi dispiace per il disagio.", "Funzionalità in manutenzione");
    }
}
