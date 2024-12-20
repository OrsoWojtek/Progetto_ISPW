package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.RoleNotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
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
    public void onLoginButtonClick(){
        LoginInfoBean bean = new LoginInfoBean();               //Istanziamento bean per il login
        LoginController controller = new LoginController();     //Istanziamento controller
        bean.setUsername(username.getText());                   //Setting del bean
        bean.setPassword(pssw.getText());
        try {
            controller.checkLogin(bean);                                                         //Se le credenziali inserite sono corrette...
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/home.fxml"))); //...Mostra la pagina di home
            Stage stage = (Stage) username.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (DataNotFoundException e){                                                                           //Altrimenti...
            showErrorPopup(e.getMessage(), "Credenziali errate");                                                  //...Mostra un popup di errore
        } catch (DataAccessException e){
            showErrorPopup(e.getMessage(),"Errore DB");
        } catch (RoleNotFoundException e){
            showErrorPopup(e.getMessage(), "Ruolo indefinito");
        } catch (IOException e){
            showErrorPopup("Non è stato possibile caricare la pagina di home.","Errore cambio pagina");
        }

    }

    //----METODO CHIAMATO AL CLICK DELLA RICHIESTA DI REGISTRAZIONE----
    @FXML
    public void onRegistReqClick(){
        showErrorPopup("La funzione di registrazione è al momento disabilitata.\nCi dispiace per il disagio.", "Funzionalità in manutenzione");
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
