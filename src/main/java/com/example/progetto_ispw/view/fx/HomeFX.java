package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.controller.HomeController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
    private static final String MAINTENANCE = "Funzionalità in manutenzione";
    private HomeController home; //Riferimento al controller applicativo

    //----INIZIALIZZAZIONE DELLA PAGINA HOME----
    @FXML
    public void initialize(){
        home = new HomeController();
        //Informazioni sull'utente corrente
        UtenteInfoBean user = home.getInfoUser();
        username.setText(username.getText()+" "+ user.getUsername()); //Mostra nella home l'username dell'utente
        try {
            catalogo = home.getCorsiFrequentati(user); //Richiesta dei corsi a cui è iscritto l'utente
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),"Errore connessione");
        }
        if (!"".equals(catalogo.getFirst().getDescrizione())){
            showCourseCatalog(); //Mostra i corsi a cui è iscritto l'utente
        }
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/"+ user.getRole().toLowerCase()+"_avatar.png")).toExternalForm()));
        optionButton.setVisible("tutor".equalsIgnoreCase(user.getRole())); //Mostra il bottone delle impostazioni dei corsi (disponibile solo per i tutor)

        initializeUI();
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try{
            home.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),"Errore connessione");
        }
    }
    //----METODO PER APRIRE LA BARRA DI RICERCA PER ISCRIVERSI AI CORSI----
    @FXML
    public void onSearchButtonClicked() {
        showMessageHandler.showError("La funzione di iscrizione ad un nuovo corso è al momento disabilitata.\nCi dispiace per il disagio.",MAINTENANCE);
    }
    //----METODO PER GESTIRE I CORSI (DISPONIBILE SOLO PER I TUTOR)----
    public void onOptionButtonClicked(){
        showMessageHandler.showError("La funzione di gestione dei corsi è al momento disabilitata.\nCi dispiace per il disagio.", MAINTENANCE);
    }

    //----METODO PER MOSTRARE IL CATALOGO DI CORSI A CUI L'UTENTE È ISCRITTO----


    //----METODO PER PASSARE ALLA PAGINA DEL CORSO DESIDERATO----
    private void goToCoursePage(String nomeCorso){
        Optional<CorsoInfoBean> corsoScelto = catalogo.stream().filter(c -> c.getNome().equals(nomeCorso)).findFirst(); //Cerca il bean del corso nel catalogo corrispondere al nome del corso selezionato dall'utente
        corsoScelto.ifPresent(home::setInfoCourse); //Passa il bean del corso al controller applicativo
        try{
            pageLoader.loadPage("corso");//...Mostra la pagina del corso
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        }
    }



    private int currentPage = 0; // Indice della pagina corrente
    private final VBox coursesContainer = new VBox(); // Contenitore per i corsi

    private void initializeUI() {
        // Configura il contenitore dinamico per i corsi
        coursesContainer.setLayoutX(742);
        coursesContainer.setLayoutY(173);
        coursesContainer.setSpacing(10); // Spazio tra i corsi
        catalogoCorsi.getChildren().add(coursesContainer);
    }

    private void showCourseCatalog() {
        // Rimuovi i precedenti corsi dal contenitore
        coursesContainer.getChildren().clear();

        // Calcola l'indice iniziale e finale dei corsi da mostrare

        int coursesPerPage = 6; // Numero massimo di corsi per pagina
        int startIndex = currentPage * coursesPerPage;
        int endIndex = Math.min(startIndex + coursesPerPage, catalogo.size());

        // Mostra i corsi correnti
        for (int i = startIndex; i < endIndex; i++) {
            CorsoInfoBean corso = catalogo.get(i);

            // Crea un rettangolo
            Rectangle rectangle = new Rectangle(597, 86);
            rectangle.setFill(Color.BLACK);
            rectangle.setStroke(Color.BLACK);

            // Crea il testo con il nome del corso
            Text text = new Text(corso.getNome());
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-weight: bold;");
            text.setStyle("-fx-font-size: 36;");
            text.setUnderline(true);

            // Centrare il testo nel rettangolo
            StackPane courseBox = new StackPane(rectangle, text);
            courseBox.setAlignment(Pos.CENTER);

            // Eventi per clic sul corso
            courseBox.setOnMouseEntered(event -> courseBox.setCursor(Cursor.HAND));
            courseBox.setOnMouseExited(event -> courseBox.setCursor(Cursor.DEFAULT));
            courseBox.setOnMouseClicked(mouseEvent -> goToCoursePage(corso.getNome()));

            // Aggiungi il rettangolo e il testo al contenitore
            coursesContainer.getChildren().add(courseBox);
        }

        // Aggiungi i bottoni di navigazione
        addNavigationButtons(endIndex);
    }

    private void addNavigationButtons(int endIndex) {
        // Rimuovi i precedenti bottoni
        coursesContainer.getChildren().removeIf(node -> node instanceof Button);

        // Bottone "Torna indietro"
        if (currentPage > 0) {
            Button backButton = new Button("Torna indietro");
            backButton.setOnAction(event -> {
                currentPage--;
                showCourseCatalog();
            });
            coursesContainer.getChildren().add(backButton);
        }

        // Bottone "Mostra altri"
        if (endIndex < catalogo.size()) {
            Button nextButton = new Button("Mostra altri");
            nextButton.setOnAction(event -> {
                currentPage++;
                showCourseCatalog();
            });
            coursesContainer.getChildren().add(nextButton);
        }
    }
}
