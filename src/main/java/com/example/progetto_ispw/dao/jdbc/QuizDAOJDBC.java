package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.QuizCreationException;
import com.example.progetto_ispw.model.Quesito;
import com.example.progetto_ispw.model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizDAOJDBC {
    private final Connection connection;

    public QuizDAOJDBC() throws ConnectionException {
        Connessione connessione = Connessione.getInstance();
        connection = connessione.getConnect();
    }
    //----METODO PER OTTENERE TUTTI I QUIZ PRESENTI NEL CORSO----
    public List<Quiz> getQuizzes(CorsoInfoBean corso) throws DataAccessException{
        List<Quiz> quizzes = new ArrayList<>();
        String query = """
        SELECT q.idquiz, q.titolo, q.durata, q.difficolta, q.argomenti,
               d.iddomanda, d.testo AS domanda_testo, d.punti,
               r.testo AS risposta_testo, r.corretta
        FROM quiz q
        JOIN domanda d ON q.idquiz = d.idquiz
        JOIN risposta r ON d.iddomanda = r.iddomanda
        WHERE q.idcorso = (SELECT corso_id FROM corso WHERE nome_corso = ?)
        ORDER BY q.idquiz, d.iddomanda;
        """;

        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, corso.getNome());
            try (ResultSet result = stm.executeQuery()) {
                Map<Integer, Quiz.Builder> quizBuilders = new HashMap<>();
                Map<Integer, Quesito.Builder> quesitoBuilders = new HashMap<>();
                int rispostaCorrente = 1;
                while (result.next()) {
                    int quizId = result.getInt("idquiz");

                    int domandaId = result.getInt("iddomanda");
                    String titolo = result.getString("titolo");
                    String difficolta = result.getString("difficolta");
                    String argomenti = result.getString("argomenti");
                    int durata = result.getInt("durata");
                    String domanda = result.getString("domanda_testo");
                    int punti = result.getInt("punti");

                    // Se il quiz non è già stato aggiunto, creiamo il suo builder
                    quizBuilders.computeIfAbsent(quizId, id -> {
                        Quiz.Builder builder = null;
                        builder = new Quiz.Builder()
                                .setTitolo(titolo)
                                .setDurata(durata)
                                .setDifficolta(difficolta)
                                .setArgomenti(argomenti);
                        return builder;
                    });

                    // Se il quesito non è già stato aggiunto, creiamo il suo builder
                    quesitoBuilders.computeIfAbsent(domandaId, id -> {
                        Quesito.Builder quesitoBuilder = null;
                        quesitoBuilder = new Quesito.Builder()
                                .setDomanda(domanda)
                                .setPunti(punti);
                        return quesitoBuilder;
                    });
                    // Aggiungiamo le risposte al quesito
                    quesitoBuilders.get(domandaId).addRisposta(
                            result.getString("risposta_testo"),
                            result.getBoolean("corretta")
                    );
                    int nRispxDom = 4; //Numero di risposte per domanda
                    if (rispostaCorrente == nRispxDom){
                        quizBuilders.get(quizId).addQuesito(quesitoBuilders.get(domandaId).build());
                        rispostaCorrente = 0;
                    }
                    rispostaCorrente++;
                }
                quizBuilders.forEach((quizId, builder) ->{
                    try {
                        quizzes.add(builder.build());
                    } catch (DataNotFoundException e) {
                        throw new QuizCreationException(e.getMessage());
                    }
                });
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore di comunicazione con il database.");
        }
        return quizzes;
    }
}
