package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: LOGIN)----
public class LoginFX extends PageManager {
    //----USERNAME UTENTE----
    @FXML
    private TextField username;
    //----PASSWORD UTENTE----
    @FXML
    private PasswordField pssw;
    @FXML
    private ImageView loginImageButton;
    @FXML
    private Button loginButton;
    @FXML
    public void initialize(){
        loginImageButton.setOnMouseClicked(mouseEvent -> loginButton.fire());
        loginButton = new Button();
        loginButton.setOnAction(e -> {
            String usernameInsert = username.getText().trim();
            String passwordInsert = pssw.getText().trim();

            // Controlla se uno dei campi è vuoto
            if (usernameInsert.isEmpty() || passwordInsert.isEmpty()) {
                showMessageHandler.showError("Una o più credenziali non sono state inserite.", ErrorCode.CREDENTIAL_NOT_FOUND);
            } else {
                onLoginButtonClick();
            }
        });
        username.setOnAction(e -> loginButton.fire());
        pssw.setOnAction(e -> loginButton.fire());
    }

    //----METODO CHIAMATO AL CLICK DEL TASTO DI LOGIN----
    @FXML
    public synchronized void onLoginButtonClick(){
        LoginInfoBean bean = new LoginInfoBean();               //Istanziamento bean per il login
        LoginController controller = new LoginController();     //Istanziamento controller
        bean.setUsername(username.getText());                   //Setting del bean
        bean.setPassword(pssw.getText());
        try {
            controller.checkLogin(bean);     //Se le credenziali inserite sono corrette...
            pageLoader.loadPage(PageID.HOME);    //...Mostra la pagina di home
        } catch (DataNotFoundException e){                                            //Altrimenti...
            showMessageHandler.showError(e.getMessage(), ErrorCode.CREDENTIAL_ERROR);   //...Mostra un popup di errore
        } catch (DataAccessException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.DB_ERROR);
        } catch (RoleNotFoundException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.UNDEF_ROLE);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
        }

    }
    //----METODO CHIAMATO AL CLICK DELLA RICHIESTA DI REGISTRAZIONE----
    @FXML
    public void onRegistReqClick(){
        showMessageHandler.showError("La funzione di registrazione è al momento disabilitata.\nCi dispiace per il disagio.", ErrorCode.MAINTENANCE);
    }
}
