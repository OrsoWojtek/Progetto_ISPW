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
    private PreparedStatement statement = null;

    public QuizDAOJDBC() throws ConnectionException {
        connection = Connessione.getInstance().getConnect();
        connessione = Connessione.getInstance();
    }
    //----METODO PER OTTENERE TUTTI I QUIZ PRESENTI NEL CORSO----
    public List<Quiz> getQuizzes(CorsoInfoBean corso) throws DataAccessException, ConnectionException, DataNotFoundException{
        ResultSet result;
        List<Quiz> quizzes = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT q.titolo, q.punteggio, q.durata, q.difficolta, q.argomenti FROM quiz q WHERE q.idcorso = (SELECT c.corso_id FROM corso c JOIN (utente_corso uc JOIN utente u ON uc.username_utente = u.username) ON c.corso_id = uc.corso_id WHERE u.username = ? AND c.nome_corso = ?);"); //Preparazione della query
            statement.setString(1, corso.getNome());                                                                   //Imposta primo parametro
            result = statement.executeQuery();                                                                                         //Esecuzione query
            if(!result.next()){                                                                                                        //Verifica se è stata restituita qualche riga
                throw new DataNotFoundException("Utente non iscritto ad alcun corso.");
            }
            do {
                int i =0;//messo tanto per fare
            } while (result.next());
        } catch (SQLException e) {
            throw new DataAccessException("Vi sono stati problemi di comunicazione con il DB.");                                        //Può verificarsi sia per errori di connessione che per operazioni errate su righe ottenute
        } finally {
            connessione.close(statement);
        }
        return quizzes;
    }
}
