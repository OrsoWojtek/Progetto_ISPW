package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.PageNotFoundException;
import com.example.progetto_ispw.view.PageManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoFX extends PageManager {
    @FXML
    private Label nomeCorso; //Nome del corso
    @FXML
    private Text sollecitaDomanda; //Testo per la richiesta di "sollecita domanda"
    @FXML
    private Text visualizzaDomande; //Testo per la richiesta di "visualizza domande"
    @FXML
    private AnchorPane catalogoQuiz;
    private List<QuizInfoBean> quizzes; //Catalogo dei quiz disponibili
    private static final String MAINTENANCE = "Funzionalità in manutenzione";
    private  CorsoPageController controller; //Riferimento al controller applicativo associato
    private int currentPage = 0; // Indice della pagina corrente dei quiz da mostrare
    private final VBox quizContainer = new VBox(); // Contenitore per i quiz
    @FXML
    public void initialize(){
        controller = new CorsoPageController();
        UtenteInfoBean user = controller.getInfoUser();
        CorsoInfoBean corso = controller.getInfoCourse();
        nomeCorso.setText(corso.getNome()+":"); //Mostra nella pagina del corso il nome del corso
        visualizzaDomande.setVisible("tutor".equalsIgnoreCase(user.getRole())); //Mostra il testo per accedere alla funzionalità disponibile solo per i tutor
        sollecitaDomanda.setVisible("studente".equalsIgnoreCase(user.getRole())); //Mostra il testo per accedere alla funzionalità disponibile solo per gli studenti
        try{
            quizzes = controller.getQuizDisponibili(corso);
            showQuizCatalog();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(), "Errrore connessione");
        } catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(), "Quiz non trovati");
        } catch (DataAccessException e){
            showMessageHandler.showError(e.getMessage(),"Errore DB");
        }
        configUI();
    }
    //---METODO PER INIZIALIZZARE IL CONTENITORE DEI QUIZ----
    private void configUI() {
        // Posizione del container
        int x = 371; //(per il maxi-schermo: 742; per altri schermi: 371)
        int y = 86; //(per il maxi-schermo: 173; per altri schermi: 86)
        // Configura il contenitore dinamico per i quiz
        quizContainer.setLayoutX(x);
        quizContainer.setLayoutY(y);
        quizContainer.setSpacing(10); // Spazio tra i quiz
        catalogoQuiz.getChildren().add(quizContainer);
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogoutButtonClicked(){
        try {
            controller.clean();
            pageLoader.loadPage("login");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),"Errore connessione");
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        controller.clearInfoCourse();
        try{
            pageLoader.loadPage("home");
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),"Pagina non trovata");
        }
    }
    @FXML
    public void onMessageBoxClicked(){
        if(sollecitaDomanda.isVisible()){
            onSollecitaDomandaClicked();
        } else {
            onVisualizzaDomandeClicked();
        }
    }
    @FXML
    public void onSollecitaDomandaClicked(){
        showMessageHandler.showError("Al momento non è possibile sollecitare domande al tutor.\nCi dispiace per il disagio.", MAINTENANCE);
    }
    @FXML
    public void onVisualizzaDomandeClicked(){
        showMessageHandler.showError("Al momento non è possibile visualizzare domande in arrivo dagli studenti.\nCi dispiace per il disagio.", MAINTENANCE);
    }
    @FXML
    public void onDescrizioneClicked(){
        showMessageHandler.showMessage(controller.getInfoCourse().getDescrizione(), "Descrizione del corso");
    }
    @FXML
    public void onTeoriaClicked(){
        showMessageHandler.showError("La teoria del corso non è al momento consultabile.\nCi dispiace per il disagio.", MAINTENANCE);
    }
    //----METODO PER MOSTRARE IL CATALOGO DI QUIZ DISPONIBILI----
    private void showQuizCatalog() {
        // Rimuovi i precedenti corsi dal contenitore
        quizContainer.getChildren().clear();

        // Calcola l'indice iniziale e finale dei quiz da mostrare

        int quizzesPerPage = 6; // Numero massimo di quiz per pagina
        int startIndx = currentPage * quizzesPerPage;
        int endIndx = Math.min(startIndx + quizzesPerPage, quizzes.size());

        // Mostra i quiz correnti
        for (int i = startIndx; i < endIndx; i++) {
            QuizInfoBean quiz = quizzes.get(i);

            // Crea un rettangolo
            int width = 298; //(per il maxi-schermo: 597; per altri schermi: 298)
            int height = 43; //(per il maxi-schermo: 86; per altri schermi: 43)
            Rectangle rectangle = new Rectangle(width, height);
            rectangle.setFill(Color.BLACK);
            rectangle.setStroke(Color.BLACK);

            // Crea il testo con il nome del quiz
            String fontSize = "18"; //(per il maxi-schermo: 36; per altri schermi: 18)
            Text titolo = new Text(quiz.getTitolo());
            titolo.setFill(Color.WHITE);
            titolo.setStyle("-fx-font-weight: bold;");
            titolo.setStyle("-fx-font-size:"+fontSize+";");
            titolo.setUnderline(true);

            // Centrare il testo nel rettangolo
            StackPane quizBox = new StackPane(rectangle, titolo);
            quizBox.setAlignment(Pos.CENTER);

            // Eventi per clic sul quiz
            quizBox.setOnMouseEntered(event -> quizBox.setCursor(Cursor.HAND));
            quizBox.setOnMouseExited(event -> quizBox.setCursor(Cursor.DEFAULT));
            //courseBox.setOnMouseClicked(mouseEvent -> );

            // Aggiungi il rettangolo e il testo al contenitore
            quizContainer.getChildren().add(quizBox);
        }

        // Aggiungi i bottoni di navigazione
        addNavigationButtons(endIndx);
    }

    private void addNavigationButtons(int endIndx) {
        // Rimuovi i precedenti bottoni
        quizContainer.getChildren().removeIf(node -> node instanceof HBox);

        // Crea un contenitore orizzontale per i bottoni
        HBox navigBox = new HBox(20); // Spazio tra i bottoni
        navigBox.setAlignment(Pos.CENTER);

        // Bottone "Torna indietro" a sinistra
        if (currentPage > 0) {
            ImageView backArrow = new ImageView(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/go_back.png")).toExternalForm());
            backArrow.setFitWidth(30);  // Larghezza desiderata
            backArrow.setFitHeight(20); // Altezza desiderata
            backArrow.setPreserveRatio(true); // Mantieni le proporzioni
            backArrow.setOnMouseEntered(event -> backArrow.setCursor(Cursor.HAND));
            backArrow.setOnMouseExited(event -> backArrow.setCursor(Cursor.DEFAULT));
            backArrow.setOnMouseClicked(mouseEvent -> {
                currentPage--;
                showQuizCatalog();
            });
            navigBox.getChildren().add(backArrow);
        }

        // Aggiungi uno "spazio vuoto" che spinge i bottoni ai bordi
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // Questo permette di spingere i bottoni ai bordi
        navigBox.getChildren().add(spacer);

        // Bottone "Mostra altri" a destra
        if (endIndx < quizzes.size()) {
            ImageView nextArrow = new ImageView(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/go_next.png")).toExternalForm());
            nextArrow.setFitWidth(30);  // Larghezza desiderata
            nextArrow.setFitHeight(20); // Altezza desiderata
            nextArrow.setPreserveRatio(true); // Mantieni le proporzioni
            nextArrow.setOnMouseEntered(event -> nextArrow.setCursor(Cursor.HAND));
            nextArrow.setOnMouseExited(event -> nextArrow.setCursor(Cursor.DEFAULT));
            nextArrow.setOnMouseClicked(mouseEvent -> {
                currentPage++;
                showQuizCatalog();
            });
            navigBox.getChildren().add(nextArrow);
        }

        // Aggiungi il contenitore con i bottoni alla lista dei corsi
        quizContainer.getChildren().add(navigBox);
    }
}
