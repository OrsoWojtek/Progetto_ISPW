package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.exception.CredentialErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO_JDBC {
    private final Connessione connessione;
    private final Connection connection;
    private PreparedStatement statement = null;

    public LoginDAO_JDBC() throws SQLException {
        connection = Connessione.getInstance().getConnect();
        connessione = Connessione.getInstance();
    }


    //----METODO PER VERIFICARE LA PRESENZA DELLE CREDENZIALI INSERITE NEL DB----
    public void isRegistered(LoginInfoBean current_cred) throws CredentialErrorException {
        boolean user_exist;
        try {
            statement = connection.prepareStatement("SELECT username, password FROM utenti WHERE username = ? AND password = ?"); //Preparazione della query
            statement.setString(1, current_cred.getUsername());                                                          //Imposta primo parametro
            statement.setString(2, current_cred.getPassword());                                                          //Imposta secondo parametro
            ResultSet result = statement.executeQuery();                                                                              //Esecuzione query
            user_exist = result.next();                                                                                               //Verifica se è stata restituita qualche riga
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la verifica del login", e);
        } finally {
            connessione.close(statement);
        }
        if(!user_exist){
            throw new CredentialErrorException("Username e/o password inseriti non corretti o non registrati. Prego riprovare.");
        }
    }
}
