package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.model.Corso;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CorsoDAOJDBC {
    private final Connessione connessione;
    private final Connection connection;
    private PreparedStatement statement = null;

    public CorsoDAOJDBC() {
        connection = Connessione.getInstance().getConnect();
        connessione = Connessione.getInstance();
    }
    //----METODO PER OTTENERE TUTTI I CORSI A CUI È ISCRITTO L'UTENTE----
    public List<Corso> getCourses(UtenteInfoBean user) throws DataNotFoundException, DataAccessException {
        ResultSet result;
        List<Corso> corsi = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT c.nome_corso, c.descrizione FROM utente u JOIN utente_corso uc ON u.username = uc.username_utente JOIN corso c ON uc.corso_id = c.corso_id WHERE u.username = ?"); //Preparazione della query
            statement.setString(1, user.getUsername());                                                                   //Imposta primo parametro
            result = statement.executeQuery();                                                                                         //Esecuzione query
            if(!result.next()){                                                                                                        //Verifica se è stata restituita qualche riga
                throw new DataNotFoundException("Utente non iscritto ad alcun corso.");
            }
            do {
                String nomeCorso = result.getString("nome_corso");
                String descrizione = result.getString("descrizione");
                corsi.add(new Corso(nomeCorso,descrizione));
            } while (result.next());
        } catch (SQLException e) {
            throw new DataAccessException("Vi sono stati problemi di comunicazione con il DB.");                                        //Può verificarsi sia per errori di connessione che per operazioni errate su righe ottenute
        } finally {
            connessione.close(statement);
        }
        return corsi;
    }
}
