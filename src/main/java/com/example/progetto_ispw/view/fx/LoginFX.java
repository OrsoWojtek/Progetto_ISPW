package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: LOGIN)----
public class LoginFX extends PageManager {
    //----USERNAME UTENTE----
    @FXML
    private TextField username;
    //----PASSWORD UTENTE----
    @FXML
    private PasswordField pssw;

    //----METODO CHIAMATO AL CLICK DEL TASTO DI LOGIN----
    @FXML
    public void onLoginButtonClick(){
        LoginInfoBean bean = new LoginInfoBean();               //Istanziamento bean per il login
        LoginController controller = new LoginController();     //Istanziamento controller
        bean.setUsername(username.getText());                   //Setting del bean
        bean.setPassword(pssw.getText());
        try {
            controller.checkLogin(bean);     //Se le credenziali inserite sono corrette...
            pageLoader.loadPage("home");    //...Mostra la pagina di home
        } catch (DataNotFoundException e){                                            //Altrimenti...
            showErrorHandler.showError(e.getMessage(), "Credenziali errate");   //...Mostra un popup di errore
        } catch (DataAccessException e){
            showErrorHandler.showError(e.getMessage(),"Errore DB");
        } catch (RoleNotFoundException e){
            showErrorHandler.showError(e.getMessage(), "Ruolo indefinito");
        } catch (PageNotFoundException e){
            showErrorHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showErrorHandler.showError(e.getMessage(),"Errore connessione");
        }

    }
    //----METODO CHIAMATO AL CLICK DELLA RICHIESTA DI REGISTRAZIONE----
    @FXML
    public void onRegistReqClick(){
        showErrorHandler.showError("La funzione di registrazione è al momento disabilitata.\nCi dispiace per il disagio.", "Funzionalità in manutenzione");
    }
}
