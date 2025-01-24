package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.controller.HomeController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    private AnchorPane catalogoCorsi; //'Sfondo' del catalogo
    private List<CorsoInfoBean> catalogo; //Catalogo dei corsi a cui è iscritto l'utente
    private HomeController home; //Riferimento al controller applicativo
    private int currentPage = 0; //Indice della pagina corrente dei corsi da mostrare
    private final VBox coursesContainer = new VBox(); //Contenitore per i corsi

    //----INIZIALIZZAZIONE DELLA PAGINA HOME----
    @FXML
    public void initialize() {
        home = new HomeController();
        try {
            //Informazioni sull'utente corrente
            UtenteInfoBean user = home.getInfoUser();
            username.setText(username.getText()+" "+ user.getUsername()); //Mostra nella home l'username dell'utente

            avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/"+ user.getRole().toLowerCase()+"_avatar.png")).toExternalForm())); //Setting dell'immagine relativa al ruolo dell'utente
            optionButton.setVisible("tutor".equalsIgnoreCase(user.getRole())); //Mostra il bottone delle impostazioni dei corsi (disponibile solo per i tutor)

            catalogo = home.getCorsiFrequentati(user); //Richiesta dei corsi a cui è iscritto l'utente
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
        } catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
            onLogoutButtonClicked();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
            onLogoutButtonClicked();
        }
        if (!"".equals(catalogo.getFirst().getDescrizione())){
            showCourseCatalog(); //Mostra i corsi a cui è iscritto l'utente
        }

        initializeUI();
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try{
            home.clean();
            pageLoader.loadPage(PageID.LOGIN);
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
        }
    }
    //----METODO PER APRIRE LA BARRA DI RICERCA PER ISCRIVERSI AI CORSI----
    @FXML
    public void onSearchButtonClicked() {
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
    }
    //----METODO PER GESTIRE I CORSI (DISPONIBILE SOLO PER I TUTOR)----
    public void onOptionButtonClicked(){
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
    }

    //----METODO PER PASSARE ALLA PAGINA DEL CORSO DESIDERATO----
    private void goToCoursePage(String nomeCorso){
        Optional<CorsoInfoBean> corsoScelto = catalogo.stream().filter(c -> c.getNome().equals(nomeCorso)).findFirst(); //Cerca il bean del corso nel catalogo corrispondere al nome del corso selezionato dall'utente
        corsoScelto.ifPresent(currentCourse -> {
            try {
                home.setInfoCourse(currentCourse); //Passa il bean del corso al controller applicativo
            } catch (DataNotFoundException e) {
                showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
                onLogoutButtonClicked();
            }catch (DataSessionCastingException e){
                showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
                onLogoutButtonClicked();
            }
        });
        try{
            pageLoader.loadPage(PageID.CORSO);//...Mostra la pagina del corso
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
        }
    }
    //---METODO PER INIZIALIZZARE IL CONTENITORE DEI CORSI----
    private void initializeUI() {
        //Posizione del container
        int x = 371; //(per il 'maxi-schermo': 742; per altri schermi: 371)
        int y = 86; //(per il 'maxi-schermo': 173; per altri schermi: 86)
        //Configura il contenitore dinamico per i corsi
        coursesContainer.setLayoutX(x);
        coursesContainer.setLayoutY(y);
        coursesContainer.setSpacing(10); //Spazio tra i corsi
        catalogoCorsi.getChildren().add(coursesContainer);
    }

    //----METODO PER MOSTRARE IL CATALOGO DI CORSI A CUI L'UTENTE È ISCRITTO----
    private void showCourseCatalog() {
        //Rimuovi i precedenti corsi dal contenitore
        coursesContainer.getChildren().clear();

        //Calcola l'indice iniziale e finale dei corsi da mostrare

        int coursesPerPage = 6; //Numero massimo di corsi per pagina
        int startIndex = currentPage * coursesPerPage;
        int endIndex = Math.min(startIndex + coursesPerPage, catalogo.size());

        //Mostra i corsi correnti
        for (int i = startIndex; i < endIndex; i++) {
            CorsoInfoBean corso = catalogo.get(i);

            //Crea un rettangolo
            int width = 298; //(per il 'maxi-schermo': 597; per altri schermi: 298)
            int height = 43; //(per il 'maxi-schermo': 86; per altri schermi: 43)
            Rectangle rectangle = new Rectangle(width, height);
            rectangle.setFill(Color.BLACK);
            rectangle.setStroke(Color.BLACK);

            //Crea il testo con il nome del corso
            String fontSize = "18"; //(per il 'maxi-schermo': 36; per altri schermi: 18)
            Text text = new Text(corso.getNome());
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-weight: bold;");
            text.setStyle("-fx-font-size:"+fontSize+";");
            text.setUnderline(true);

            //Centrare il testo nel rettangolo
            StackPane courseBox = new StackPane(rectangle, text);
            courseBox.setAlignment(Pos.CENTER);

            //Eventi per clic sul corso
            courseBox.setOnMouseEntered(event -> courseBox.setCursor(Cursor.HAND));
            courseBox.setOnMouseExited(event -> courseBox.setCursor(Cursor.DEFAULT));
            courseBox.setOnMouseClicked(mouseEvent -> goToCoursePage(corso.getNome()));

            //Aggiungi il rettangolo e il testo al contenitore
            coursesContainer.getChildren().add(courseBox);
        }

        //Aggiungi i bottoni di navigazione
        addNavigationButtons(endIndex);
    }

    private void addNavigationButtons(int endIndex) {
        //Rimuovi i precedenti bottoni
        coursesContainer.getChildren().removeIf(node -> node instanceof HBox);

        //Crea un contenitore orizzontale per i bottoni
        HBox navigationBox = new HBox(20); //Spazio tra i bottoni
        navigationBox.setAlignment(Pos.CENTER);

        //Bottone "Torna indietro" a sinistra
        if (currentPage > 0) {
            ImageView backButton = new ImageView(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/go_back.png")).toExternalForm());
            backButton.setFitWidth(30);  //Larghezza desiderata
            backButton.setFitHeight(20); //Altezza desiderata
            backButton.setPreserveRatio(true); //Mantieni le proporzioni
            backButton.setOnMouseEntered(event -> backButton.setCursor(Cursor.HAND));
            backButton.setOnMouseExited(event -> backButton.setCursor(Cursor.DEFAULT));
            backButton.setOnMouseClicked(mouseEvent -> {
                currentPage--;
                showCourseCatalog();
            });
            navigationBox.getChildren().add(backButton);
        }

        //Aggiungi uno "spazio vuoto" che spinge i bottoni ai bordi
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  //Questo permette di spingere i bottoni ai bordi
        navigationBox.getChildren().add(spacer);

        //Bottone "Mostra altri" a destra
        if (endIndex < catalogo.size()) {
            ImageView nextButton = new ImageView(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/go_next.png")).toExternalForm());
            nextButton.setFitWidth(30);  //Larghezza desiderata
            nextButton.setFitHeight(20); //Altezza desiderata
            nextButton.setPreserveRatio(true); //Mantieni le proporzioni
            nextButton.setOnMouseEntered(event -> nextButton.setCursor(Cursor.HAND));
            nextButton.setOnMouseExited(event -> nextButton.setCursor(Cursor.DEFAULT));
            nextButton.setOnMouseClicked(mouseEvent -> {
                currentPage++;
                showCourseCatalog();
            });
            navigationBox.getChildren().add(nextButton);
        }

        //Aggiungi il contenitore con i bottoni alla lista dei corsi
        coursesContainer.getChildren().add(navigationBox);
    }
}
