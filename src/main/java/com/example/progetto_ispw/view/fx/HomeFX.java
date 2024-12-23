package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.controller.HomeController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: HOME)----
public class HomeFX extends PageManager {
    @FXML
    private Label username; //Username dell'utente loggato
    @FXML
    private ImageView avatar; //Avatar dell'utente loggato
    @FXML
    private ImageView optionButton; //Bottone delle impostazioni dei corsi (disponibile solo per i tutor)
    @FXML
    private AnchorPane catalogoCorsi;
    private List<CorsoInfoBean> catalogo; //Catalogo dei corsi a cui è iscritto l'utente

    //----INIZIALIZZAZIONE DELLA PAGINA HOME----
    @FXML
    public void initialize(){
        HomeController home = new HomeController();
        //Informazioni sull'utente corrente
        UtenteInfoBean user = home.getInfoUser();
        username.setText(username.getText()+" "+ user.getUsername()); //Mostra nella home l'username dell'utente
        try {
            catalogo = home.getCorsiFrequentati(user); //Richiesta dei corsi a cui è iscritto l'utente
        } catch (ConnectionException e){
            showErrorHandler.showError(e.getMessage(),"Errore connessione");
        }
        if (!"".equals(catalogo.getFirst().getDescrizione())){
            showCourseCatalog(); //Mostra i corsi a cui è iscritto l'utente
        }
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/"+ user.getRole().toLowerCase()+"_avatar.png")).toExternalForm()));
        optionButton.setVisible("tutor".equalsIgnoreCase(user.getRole())); //Mostra il bottone delle impostazioni dei corsi (disponibile solo per i tutor)
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        HomeController homeController = new HomeController();
        try{
            homeController.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showErrorHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showErrorHandler.showError(e.getMessage(),"Errore connessione");
        }
    }
    //----METODO PER APRIRE LA BARRA DI RICERCA PER ISCRIVERSI AI CORSI----
    @FXML
    public void onSearchButtonClicked() {
        showErrorHandler.showError("La funzione di iscrizione ad un nuovo corso è al momento disabilitata.\nCi dispiace per il disagio.","Funzionalità in manutenzione");
    }
    //----METODO PER GESTIRE I CORSI (DISPONIBILE SOLO PER I TUTOR)----
    public void onOptionButtonClicked(){
        showErrorHandler.showError("La funzione di gestione dei corsi è al momento disabilitata.\nCi dispiace per il disagio.", "Funzionalità in manutenzione");
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


            // Aggiungi un gestore di eventi per il mouse sopra il rettangolo
            rectangle.setOnMouseEntered(event -> {
                rectangle.setCursor(Cursor.HAND); // Cambia il cursore a mano aperta
            });

            // Aggiungi un gestore di eventi per il mouse che esce dal rettangolo
            rectangle.setOnMouseExited(event -> {
                rectangle.setCursor(Cursor.DEFAULT); // Ritorna il cursore predefinito
            });

            // Aggiungi un gestore di eventi per il mouse sopra il nome del corso
            text.setOnMouseEntered(event -> {
                text.setCursor(Cursor.HAND); // Cambia il cursore a mano aperta
            });

            // Aggiungi un gestore di eventi per il mouse che esce dal nome del corso
            text.setOnMouseExited(event -> {
                text.setCursor(Cursor.DEFAULT); // Ritorna il cursore predefinito
            });


            //Cliccando sul testo o sul rettangolo relativo al corso si apre la pagina del corso stesso
            text.setOnMouseClicked(mouseEvent -> goToCoursePage(text.getText()));
            rectangle.setOnMouseClicked(text::fireEvent);


            // Aggiungi rettangolo e testo al contenitore
            catalogoCorsi.getChildren().addAll(rectangle,text);

            // Aggiorna la posizione per il prossimo rettangolo
            y += 123; // Sposta verso il basso per evitare sovrapposizioni
        }
    }

    //----METODO PER PASSARE ALLA PAGINA DEL CORSO DESIDERATO----
    private void goToCoursePage(String nomeCorso){
        HomeController home = new HomeController();
        Optional<CorsoInfoBean> corsoScelto = catalogo.stream().filter(c -> c.getNome().equals(nomeCorso)).findFirst(); //Cerca il bean del corso nel catalogo corrispondere al nome del corso selezionato dall'utente
        corsoScelto.ifPresent(home::setInfoCourse); //Passa il bean del corso al controller applicativo
        try{
            pageLoader.loadPage("corso");//...Mostra la pagina del corso
        } catch (PageNotFoundException e){
            showErrorHandler.showError(e.getMessage(),"Pagina non trovata");
        }
    }
}
