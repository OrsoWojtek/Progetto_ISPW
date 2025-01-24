package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.exception.*;
import com.example.progetto_ispw.model.Utente;
import com.example.progetto_ispw.pattern.factory.UtenteFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAOJDBC extends DAOJDBC{
    private PreparedStatement statement = null;

    public UtenteDAOJDBC() throws ConnectionException {
        connessione = getConnessione();
        connection = connessione.getConnect();
    }

    //----METODO PER VERIFICARE LA PRESENZA DELLE CREDENZIALI INSERITE NEL DB----
    public Utente getNewUtente(LoginInfoBean currentCred) throws DataNotFoundException, DataAccessException, RoleNotFoundException, ConnectionException {
        String ruolo;
        ResultSet result;
        try {
            statement = connection.prepareStatement("SELECT tipo FROM utente WHERE username = ? AND password = ?");               //Preparazione della query
            statement.setString(1, currentCred.getUsername());                                                           //Imposta primo parametro
            statement.setString(2, currentCred.getPassword());                                                           //Imposta secondo parametro
            result = statement.executeQuery();                                                                                        //Esecuzione query
            if(!result.next()){                                                                                                       //Verifica se è stata restituita qualche riga
                throw new DataNotFoundException("Username e/o password inseriti non corretti o non registrati. Prego riprovare.");
            }
            ruolo = result.getString("tipo");                                                                             //Prelevato il tipo di utente
            result.close();
        } catch (SQLException e) {
            throw new DataAccessException("Vi sono stati problemi di comunicazione con il DB.");                                    //Può verificarsi sia per errori di connessione che per operazioni errate su righe ottenute
        } finally {
            connessione.close(statement);
        }
        if("S".equals(ruolo)){
            ruolo = "Studente";
        } else if ("T".equals(ruolo)) {
            ruolo = "Tutor";
        }
        return UtenteFactory.getFactory(ruolo).createUtente(currentCred.getUsername(),currentCred.getPassword());
    }
}
