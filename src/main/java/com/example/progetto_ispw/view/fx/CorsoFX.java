package com.example.progetto_ispw.view.fx;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.NotificheInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.constants.ErrorCode;
import com.example.progetto_ispw.constants.PageID;
import com.example.progetto_ispw.constants.StandardMessagge;
import com.example.progetto_ispw.constants.UserRole;
import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.view.handler.shortcut.concrete.BaseShortcutHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

//----CONTROLLER GRAFICO SECONDO IL PATTERN MVC PER LA GESTIONE DELLE INTERAZIONI DELL'UTENTE CON IL SISTEMA (CASO SPECIFICO: CORSO)----
public class CorsoFX extends BaseShortcutHandler {
    @FXML
    private Label nomeCorso; //Nome del corso
    @FXML
    private Text sollecitaDomanda; //Testo per la richiesta di "sollecita domanda"
    @FXML
    private Text visualizzaNotifiche; //Testo per la richiesta di "visualizza notifiche"
    @FXML
    private AnchorPane catalogoQuiz; //'Sfondo' del catalogo
    @FXML
    private ImageView scoreHead; //'Testa' della colonna dei punteggi
    @FXML
    private ImageView plusButton; //Tasto per l'aggiunta di quiz
    private CorsoInfoBean corso; //Riferimento al corso della pagina
    private List<QuizInfoBean> quizzes; //Catalogo dei quiz disponibili
    private  CorsoPageController controller; //Riferimento al controller applicativo associato
    private int currentPage = 0;//Indice della pagina corrente dei quiz da mostrare
    private UtenteInfoBean user; //Riferimento all'utente loggato
    private final VBox quizContainer = new VBox(); //Contenitore per i quiz
    @FXML
    @Override
    public void initialize(){
        controller = new CorsoPageController();
        try{
            user = controller.getInfoUser();
            corso = controller.getInfoCourse();
            nomeCorso.setText(corso.getNome()+":"); //Mostra nella pagina del corso il nome del corso
            visualizzaNotifiche.setVisible(UserRole.TUTOR.getValue().equalsIgnoreCase(user.getRole())); //Mostra il testo per accedere alla funzionalità disponibile solo per i tutor
            sollecitaDomanda.setVisible(UserRole.STUDENTE.getValue().equalsIgnoreCase(user.getRole())); //Mostra il testo per accedere alla funzionalità disponibile solo per gli studenti
            scoreHead.setVisible(UserRole.STUDENTE.getValue().equalsIgnoreCase(user.getRole())); //Mostra la testa della colonna degli score solo per gli studenti
            plusButton.setVisible(UserRole.TUTOR.getValue().equalsIgnoreCase(user.getRole())); //Mostra il tasto per aggiungere quiz al corso (disponibile solo per i tutor)
            quizzes = controller.getQuizDisponibili(corso,user);
            showQuizCatalog();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            onLogout();
        } catch (QuizCreationException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.QUIZ_NOT_FOUND);
            onLogout();
        } catch (DataAccessException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.DB_ERROR);
            onLogout();
        } catch (DataNotFoundException e) {
            showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
            onLogout();
        }catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
            onLogout();
        }
        configUI();
    }
    //---METODO PER INIZIALIZZARE IL CONTENITORE DEI QUIZ----
    private void configUI() {
        //Posizione del container
        int x = 371; //(per il maxi-schermo: 742; per altri schermi: 371)
        int y = 86; //(per il maxi-schermo: 173; per altri schermi: 86)
        //Configura il contenitore dinamico per i quiz
        quizContainer.setLayoutX(x);
        quizContainer.setLayoutY(y);
        quizContainer.setSpacing(10); //Spazio tra i quiz
        catalogoQuiz.getChildren().add(quizContainer);
    }
    //----METODO PER EFFETTUARE IL LOGOUT----
    @FXML
    public void onLogout(){
        try {
            controller.clean();
            logout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.CONNECTION);
            System.exit(1);
        }
    }
    //----METODO PER TORNARE ALLA HOME----
    @FXML
    public void onHomeButtonClicked(){
        controller.clearInfoCourse();
        home();
    }
    @FXML
    public void onMessageBoxClicked(){
        if(sollecitaDomanda.isVisible()){
            onSollecitaDomandaClicked();
        } else {
            onVisualizzaNotificheClicked();
        }
    }
    @FXML
    public void onSollecitaDomandaClicked(){
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
    }
    @FXML
    public void onVisualizzaNotificheClicked(){
        try {
            NotificheInfoBean notifiche = controller.getInfoNotifiche();
            showMessageHandler.showMessage(notifiche.getNotifiche(),"Notifiche");
        } catch (DataAccessException e) {
            showMessageHandler.showError(e.getMessage(),ErrorCode.DB_ERROR);
            onLogout();
        } catch (ConnectionException e){
            showMessageHandler.showError(e.getMessage(), ErrorCode.CONNECTION);
            onLogout();
        } catch (DataNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
            onLogout();
        } catch (DataSessionCastingException e){
            showMessageHandler.showError(StandardMessagge.CASTING.getValue(), ErrorCode.CASTING);
            onLogout();
        }
    }
    @FXML
    public void onDescrizioneClicked(){
        showMessageHandler.showMessage(corso.getDescrizione(), "Descrizione del corso");
    }
    @FXML
    public void onTeoriaClicked(){
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
    }
    //----METODO PER ANDARE ALLA PAGINA DI CREAZIONE DEI QUIZ----
    @FXML
    public void onPlusButtonClicked(){
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
    }
    //----METODO PER ANDARE ALLA PAGINA DI MODIFICA DEI QUIZ----
    @FXML
    public void onModificaClicked(){
        showMessageHandler.showError(StandardMessagge.MAINTENANCE.getValue(), ErrorCode.MAINTENANCE);
    }
    //----METODO PER PASSARE ALLA PAGINA DEL QUIZ DESIDERATO----
    private void goToQuizPage(String nomeQuiz){
        Optional<QuizInfoBean> quizScelto = quizzes.stream().filter(c -> c.getTitolo().equals(nomeQuiz)).findFirst(); //Cerca il bean del quiz nel catalogo corrispondere al nome del quiz selezionato dall'utente
        quizScelto.ifPresent(currentQuiz -> {
            try {
                controller.setInfoQuiz(currentQuiz); //Passa il bean del quiz al controller applicativo
            } catch (DataNotFoundException e) {
                showMessageHandler.showError(e.getMessage(),ErrorCode.SESSION);
                onLogout();
            } catch (DataSessionCastingException e){
                showMessageHandler.showError(StandardMessagge.CASTING.getValue(),ErrorCode.CASTING);
                onLogout();
            }
        });
        try{
            pageLoader.loadPage(PageID.QUIZ);//...Mostra la pagina del quiz
        } catch (PageNotFoundException e){
            showMessageHandler.showError(e.getMessage(),ErrorCode.PAGE_NOT_FOUND);
            onLogout();
        }
    }

    //----METODO PER MOSTRARE IL CATALOGO DI QUIZ DISPONIBILI----
    private void showQuizCatalog() {
        //Rimuovi i precedenti quiz dal contenitore
        quizContainer.getChildren().clear();

        //Calcola l'indice iniziale e finale dei quiz da mostrare
        int quizzesPerPage = 4; //Numero massimo di quiz per pagina
        int startIndx = currentPage * quizzesPerPage;
        int endIndx = Math.min(startIndx + quizzesPerPage, quizzes.size());

        //Mostra i quiz correnti
        for (int i = startIndx; i < endIndx; i++) {
            QuizInfoBean quiz = quizzes.get(i);

            //Larghezza massima per entrambi gli oggetti (rettangolo ed ellisse)
            int width = 298; //(per il maxi-schermo: 597; per altri schermi: 298)

            //Font per i testi
            String fontSize = "18"; //(per il maxi-schermo: 36; per altri schermi: 18)
            String fontWeightStyle = "-fx-font-weight: bold;";
            String fontSizeStyle ="-fx-font-size:" + fontSize + ";" ;

            //Rettangolo (Titolo)
            Rectangle rectTitolo = new Rectangle(187, 43);
            rectTitolo.setFill(Color.BLACK);

            //Ellisse (Punteggio)
            Ellipse ellipsePunteggio = new Ellipse(53, 27);
            ellipsePunteggio.setFill(Color.valueOf("871414"));

            //Crea il testo per il titolo del quiz
            Text titolo = new Text(quiz.getTitolo());
            titolo.setFill(Color.WHITE);
            titolo.setStyle(fontWeightStyle);
            titolo.setStyle(fontSizeStyle);

            Text testoColonna;
            StackPane stackColonna; //Riquadro per il punteggio o tasto opzioni
            if(UserRole.STUDENTE.getValue().equalsIgnoreCase(user.getRole())){
                //Crea il testo per il punteggio
                testoColonna = new Text(quiz.getPunteggioStudente() + "/" + quiz.getPunteggio());
            } else {
                //Crea il testo per le opzioni
                testoColonna = new Text("Modifica");
            }

            testoColonna.setFill(Color.WHITE);
            testoColonna.setStyle(fontWeightStyle);
            testoColonna.setStyle(fontSizeStyle);

            //Aggiungi il testo al rispettivo riquadro
            StackPane stackTitolo = new StackPane(rectTitolo, titolo);
            stackTitolo.setAlignment(Pos.CENTER);

            stackColonna = new StackPane(ellipsePunteggio, testoColonna);
            stackColonna.setAlignment(Pos.CENTER);

            //Aggiungi gli eventi per il titolo (ritorna alla pagina del quiz)
            stackTitolo.setOnMouseEntered(event -> stackTitolo.setCursor(Cursor.HAND));
            stackTitolo.setOnMouseExited(event -> stackTitolo.setCursor(Cursor.DEFAULT));
            if(UserRole.STUDENTE.getValue().equalsIgnoreCase(user.getRole())) {
                stackTitolo.setOnMouseClicked(mouseEvent -> goToQuizPage(quiz.getTitolo())); //Porta alla pagina del quiz
            }

            //Aggiungi gli eventi per il punteggio oppure opzioni
            stackColonna.setOnMouseEntered(event -> stackColonna.setCursor(Cursor.HAND));
            stackColonna.setOnMouseExited(event -> stackColonna.setCursor(Cursor.DEFAULT));
            if(UserRole.TUTOR.getValue().equalsIgnoreCase(user.getRole())) {
                stackColonna.setOnMouseClicked(mouseEvent -> onModificaClicked());
            }

            //Crea un HBox per affiancare i due rettangoli
            HBox quizBox = new HBox(stackTitolo, stackColonna);
            quizBox.setSpacing(10); //Spazio tra il titolo e il punteggio
            quizBox.setAlignment(Pos.CENTER);

            //Aggiungi una linea orizzontale sotto la coppia
            Line separatore = new Line(0, 0, width, 0); //Linea lunga quanto il rettangolo totale
            separatore.setStroke(Color.GRAY); //Colore della linea
            separatore.setStrokeWidth(1); //Spessore della linea

            //Aggiungi l'HBox al contenitore
            Platform.runLater(() -> {
                quizContainer.getChildren().add(quizBox);
                quizContainer.getChildren().add(separatore);
            });
        }

        //Aggiungi i bottoni di navigazione
        addNavigationButtons(endIndx);
    }


    private void addNavigationButtons(int endIndx) {
        //Rimuovi i precedenti bottoni
        quizContainer.getChildren().removeIf(node -> node instanceof HBox);

        //Crea un contenitore orizzontale per i bottoni
        HBox navigBox = new HBox(20); //Spazio tra i bottoni
        navigBox.setAlignment(Pos.CENTER);

        //Bottone "Torna indietro" a sinistra
        if (currentPage > 0) {
            ImageView backArrow = new ImageView(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/go_back.png")).toExternalForm());
            backArrow.setFitWidth(30);  //Larghezza desiderata
            backArrow.setFitHeight(20); //Altezza desiderata
            backArrow.setPreserveRatio(true); //Mantieni le proporzioni
            backArrow.setOnMouseEntered(event -> backArrow.setCursor(Cursor.HAND));
            backArrow.setOnMouseExited(event -> backArrow.setCursor(Cursor.DEFAULT));
            backArrow.setOnMouseClicked(mouseEvent -> {
                currentPage--;
                showQuizCatalog();
            });
            navigBox.getChildren().add(backArrow);
        }

        //Aggiungi uno "spazio vuoto" che spinge i bottoni ai bordi
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  //Questo permette di spingere i bottoni ai bordi
        navigBox.getChildren().add(spacer);

        //Bottone "Mostra altri" a destra
        if (endIndx < quizzes.size()) {
            ImageView nextArrow = new ImageView(Objects.requireNonNull(getClass().getResource("/com/example/progetto_ispw/images/go_next.png")).toExternalForm());
            nextArrow.setFitWidth(30);  //Larghezza desiderata
            nextArrow.setFitHeight(20); //Altezza desiderata
            nextArrow.setPreserveRatio(true); //Mantieni le proporzioni
            nextArrow.setOnMouseEntered(event -> nextArrow.setCursor(Cursor.HAND));
            nextArrow.setOnMouseExited(event -> nextArrow.setCursor(Cursor.DEFAULT));
            nextArrow.setOnMouseClicked(mouseEvent -> {
                currentPage++;
                showQuizCatalog();
            });
            navigBox.getChildren().add(nextArrow);
        }

        //Aggiungi il contenitore con i bottoni alla lista dei corsi
        Platform.runLater(() -> quizContainer.getChildren().add(navigBox));
    }
}
