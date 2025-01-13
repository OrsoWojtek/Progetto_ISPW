package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAOJDBC {
    private final Connessione connessione;
    private final Connection connection;
    private PreparedStatement stmParziale = null;
    private PreparedStatement stmDomande = null;
    private PreparedStatement stmRisposte = null;

    public QuizDAOJDBC() throws ConnectionException {
        connection = Connessione.getInstance().getConnect();
        connessione = Connessione.getInstance();
    }
    //----METODO PER OTTENERE TUTTI I QUIZ PRESENTI NEL CORSO----
    public List<Quiz> getQuizzes(CorsoInfoBean corso) throws DataAccessException, ConnectionException, DataNotFoundException{
        ResultSet resultParziale;
        ResultSet resultDomande;
        ResultSet resultRisposte;
        List<Quiz> quizzes = new ArrayList<>();
        try {
            stmParziale = connection.prepareStatement("SELECT q.idquiz, q.titolo, q.durata, q.difficolta, q.argomenti FROM quiz q WHERE q.idcorso = (SELECT c.corso_id FROM corso c WHERE c.nome_corso = ?);"); //Preparazione della query per prelevare parte delle informazioni per la creazione dei quiz
            stmParziale.setString(1, corso.getNome());                                                                    //Imposta primo parametro
            resultParziale = stmParziale.executeQuery();                                                                               //Esecuzione query
            if(!resultParziale.next()){         //Verifica se è stata restituita qualche riga
                throw new DataNotFoundException("Quiz non presenti.");
            }
            int quizId;
            int domandaId;
            String titolo;
            int durata;
            String difficolta;
            String argomenti;
            String domanda;
            List<String> risposte;
            List<Boolean> corrette;
            int punteggio;
            //----INIZIO CREAZIONE DELLA LISTA DI QUIZ----
            do {
                quizId = resultParziale.getInt("idquiz"); //Uso della PK per scorrere tra i quiz disponibili nel corso
                titolo = resultParziale.getString("titolo");
                durata = resultParziale.getInt("durata");
                difficolta = resultParziale.getString("difficolta");
                argomenti = resultParziale.getString("argomenti");
                Quiz.Builder builder = new Quiz.Builder() //Settaggio di alcuni dei paramentri per la creazione del quiz
                        .setTitolo(titolo)
                        .setArgomenti(argomenti)
                        .setDifficolta(difficolta)
                        .setDurata(durata);
                stmDomande = connection.prepareStatement("SELECT d.iddomanda, d.testo, d.punti FROM domanda d WHERE d.idquiz = ?;"); //Preparazione della query per prelevare testo e punti delle domande di un quiz  del corso
                stmDomande.setInt(1, quizId);                                                                    //Imposta primo parametro
                resultDomande = stmDomande.executeQuery();                                                                  //Esecuzione query
                if (!resultDomande.next()){     //Verifica se è stata restituita qualche riga
                    throw new DataNotFoundException("ToE:#1\nDati necessari alla corretta ricerca dei quiz non trovati.");
                }
                do {
                    domanda = resultDomande.getString("testo");
                    punteggio = resultDomande.getInt("punti");
                    domandaId = resultDomande.getInt("iddomanda"); //Uso della PK per scorrere tra le domande presenti nel quiz
                    stmRisposte = connection.prepareStatement("SELECT r.testo, r.corretta FROM risposta r WHERE r.iddomanda = ?;"); //Preparazione della query per prelevare testo e correttezza delle risposte di un quesito di un quiz del corso
                    stmRisposte.setInt(1, domandaId);                                                                    //Imposta primo parametro
                    resultRisposte = stmRisposte.executeQuery();                                                                     //Esecuzione query
                    if (!resultRisposte.next()){     //Verifica se è stata restituita qualche riga
                        throw new DataNotFoundException("ToE:#2\nDati necessari alla corretta ricerca dei quiz non trovati.");
                    }
                    risposte = new ArrayList<>();
                    corrette = new ArrayList<>();
                    do {
                        risposte.add(resultRisposte.getString("testo"));
                        corrette.add(resultRisposte.getBoolean("corretta"));
                    } while (resultRisposte.next());
                    builder.addQuesito(domanda,risposte,corrette,punteggio); //Aggiunta del quesito nel builder del quiz
                }while (resultDomande.next());
                quizzes.add(builder.build()); //Creazione e aggiunta del quiz nella lista di quiz
            } while (resultParziale.next());
            //----FINE CREAZIONE DELLA LISTA DI QUIZ----
            resultDomande.close();
            resultRisposte.close();
            resultParziale.close();
        } catch (SQLException e) {
            throw new DataAccessException("Vi sono stati problemi di comunicazione con il DB.");                                        //Può verificarsi sia per errori di connessione che per operazioni errate su righe ottenute
        } finally {
            connessione.close(stmParziale);
            connessione.close(stmDomande);
            connessione.close(stmRisposte);
        }
        return quizzes;
    }
}
