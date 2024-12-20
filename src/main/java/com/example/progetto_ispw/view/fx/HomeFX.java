package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.controller.HomeController;
import com.example.progetto_ispw.model.Sessione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: HOME)----
public class HomeFX {
    @FXML
    private Label username; //Username dell'utente loggato
    @FXML
    private ImageView avatar; //Avatar dell'utente loggato
    @FXML
    private ImageView optionButton; //Bottone delle impostazioni dei corsi (disponibile solo per i tutor)
    @FXML
    private AnchorPane catalogoCorsi;
    private List<CorsoInfoBean> catalogo; //Catalogo dei corsi a cui è iscritto l'utente
    private UtenteInfoBean user;
    //----INIZIALIZZAZIONE DELLA PAGINA HOME----
    @FXML
    public void initialize(){
        HomeController home = new HomeController();
        user = Sessione.getUser();
        username.setText(username.getText()+" "+user.getUsername()); //Mostra nella home l'username dell'utente
        catalogo = home.getCorsiFrequentati(user); //Richiesta dei corsi a cui è iscritto l'utente
        if (!"".equals(catalogo.getFirst().getDescrizione())){
            showCourseCatalog();
        }
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/"+user.getRole().toLowerCase()+"_avatar.png")).toExternalForm()));
        optionButton.setVisible("tutor".equalsIgnoreCase(user.getRole()));
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            Sessione.clear(); //Cancello le informazioni riguardanti la sessione
            Connessione conn = Connessione.getInstance();
            conn.closeConnection(); //Chiudo definitivamente la connessione con il db
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml"))); //Torna alla pagina di login
            Stage stage = (Stage) username.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            showErrorPopup("Errore nell'apertura della pagina di login","Pagina non trovata");
        }
    }
    //----METODO PER APRIRE LA BARRA DI RICERCA PER ISCRIVERSI AI CORSI----
    @FXML
    public void onSearchButtonClicked() {
        showErrorPopup("La funzione di iscrizione ad un nuovo corso è al momento disabilitata.\nCi dispiace per il disagio.","Funzionalità in manutenzione");
    }
    //----METODO PER GESTIRE I CORSI (DISPONIBILE SOLO PER I TUTOR)----
    public void onOptionButtonClicked(){
        showErrorPopup("La funzione di gestione dei corsi è al momento disabilitata.\nCi dispiace per il disagio.", "Funzionalità in manutenzione");
    }
    //----METODO PER MOSTRARE IL CATALOGO DI CORSI A CUI L'UTENTE È ISCRITTO----
    private void showCourseCatalog(){
        // Posizione iniziale dei rettangoli
        double x = 742;
        double y = 173;
        double rectWidth = 597;
        double rectHeight = 86;

        // Creazione dei rettangoli e testo per ogni corso
        for (CorsoInfoBean corso : catalogo) {
            // Crea un rettangolo
            Rectangle rectangle = new Rectangle(x, y, rectWidth, rectHeight);
            rectangle.setFill(Color.BLACK);
            rectangle.setStroke(Color.BLACK);
            // Crea il testo con il nome del corso
            Text text = new Text(corso.getNome()); // Posiziona il testo dentro il rettangolo
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-weight: bold;");
            text.setStyle("-fx-font-size: 36;");
            text.setUnderline(true);

            // Centrare il testo nel rettangolo
            double textWidth = text.getBoundsInLocal().getWidth();
            double textHeight = text.getBoundsInLocal().getHeight();
            text.setX(x + (rectWidth - textWidth) / 2); // Centra orizzontalmente
            text.setY(y + (rectHeight - textHeight) / 2 + textHeight); // Centra verticalmente

            text.setTextAlignment(TextAlignment.CENTER);

            // Aggiungi rettangolo e testo al contenitore
            catalogoCorsi.getChildren().addAll(rectangle, text);

            // Aggiorna la posizione per il prossimo rettangolo
            y += 123; // Sposta verso il basso per evitare sovrapposizioni
        }
    }
    //----METODO PER MOSTRARE MESSAGGI DI ERRORE----
    private void showErrorPopup(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        //Mostra il popup e attendi la chiusura
        alert.showAndWait();
    }
}
