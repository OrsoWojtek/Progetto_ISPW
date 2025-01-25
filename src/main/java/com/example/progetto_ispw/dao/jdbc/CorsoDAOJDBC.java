package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.NotificheInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.dao.CorsoDAO;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.UpdateDataException;
import com.example.progetto_ispw.model.Corso;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CorsoDAOJDBC extends DAOJDBC implements CorsoDAO {
    private PreparedStatement statement = null;

    public CorsoDAOJDBC() throws ConnectionException {
        connessione = getConnessione();
        connection = connessione.getConnect();
    }
    //----METODO PER OTTENERE TUTTI I CORSI A CUI È ISCRITTO L'UTENTE----
    @Override
    public List<Corso> getCourses(UtenteInfoBean user) throws DataNotFoundException, DataAccessException, ConnectionException {
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
            result.close();
        } catch (SQLException e) {
            throw new DataAccessException("Vi sono stati problemi di comunicazione con il DB.");                                        //Può verificarsi sia per errori di connessione che per operazioni errate su righe ottenute
        } finally {
            connessione.close(statement);
        }
        return corsi;
    }
    @Override
    public NotificheInfoBean getNotifiche(UtenteInfoBean utente, CorsoInfoBean corso) throws DataAccessException {
        String query = """
        SELECT uc.notifiche
        FROM utente_corso uc
        JOIN corso c ON uc.corso_id = c.corso_id
        WHERE uc.username_utente = ? AND c.nome_corso = ?;
        """;
        NotificheInfoBean notificheInfoBean = new NotificheInfoBean();
        notificheInfoBean.setNotifiche("Ultime notifiche: \n");
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, utente.getUsername());
            stm.setString(2, corso.getNome());
            try (ResultSet result = stm.executeQuery()) {
                if(result.next()) {
                    notificheInfoBean.addNotifiche(result.getString("notifiche"));
                }
            }
        }catch (SQLException e) {
            throw new DataAccessException("Errore di comunicazione con il database.");
        }
        return notificheInfoBean;
    }
    @Override
    public void updateNotifiche(UtenteInfoBean utente, CorsoInfoBean corso, QuizInfoBean quiz) throws DataAccessException, UpdateDataException {
        String query = """
        UPDATE utente_corso uc
        JOIN corso c ON uc.corso_id = c.corso_id
        JOIN utente u ON uc.username_utente = u.username
        SET uc.notifiche = ?
        WHERE c.nome_corso = ? AND u.tipo = ?;
        """;
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            String notifica = quiz.getTitolo()+" sostenuto di recente dall'utente: "+utente.getUsername();
            stm.setString(1,notifica);
            stm.setString(2,corso.getNome());
            stm.setString(3,"T" );
            int rowsAffected = stm.executeUpdate();
            if(rowsAffected == 0){
                throw new UpdateDataException("Non è stata aggiornata alcuna riga nel db");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore di comunicazione con il database.");
        }
    }
}
