package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.CredentialErrorException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: LOGIN)----
public class LoginFX {
    //----USERNAME UTENTE----
    @FXML
    private TextField username;
    //----PASSWORD UTENTE----
    @FXML
    private PasswordField pssw;

    //----METODO CHIAMATO AL CLICK DEL TASTO DI LOGIN----
    @FXML
    public void onLoginButtonClick() throws Exception{
        LoginInfoBean bean = new LoginInfoBean();               //Istanziamento bean per il login
        LoginController controller = new LoginController();     //Istanziamento controller
        bean.setUsername(username.getText());                   //Setting del bean
        bean.setPassword(pssw.getText());
        try {
            controller.checkLogin(bean);                                                                                //Se le credenziali inserite sono corrette...
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/home_studente.fxml")));          //...Mostra la pagina di home
            Stage stage = (Stage) username.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (CredentialErrorException e){                                                                           //Altrimenti...
            showErrorPopup(e.getMessage(), "Errore di login");                                                     //...Mostra un popup di errore
        }
    }

    //----METODO CHIAMATO AL CLICK DELLA RICHIESTA DI REGISTRAZIONE----
    @FXML
    public void onRegistReqClick(){
        showErrorPopup("Questa funzionalità non è stata ancora implementata.\nCi dispiace per il disagio.", "Funzionalità inesistente");
    }
    //----METODO PER POPUP DI ERRORE----
    private void showErrorPopup(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
}
