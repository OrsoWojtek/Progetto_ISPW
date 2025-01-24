package com.example.progetto_ispw.connessione;

import com.example.progetto_ispw.exception.ConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

//----CLASSE PER LA CONNESSIONE AL DB----
public class ConnessioneJDBC extends PersistenceConnectionManager{
    private Connection connect = null; //----CONNESSIONE AL DB


    //----COSTRUTTORE IN CUI ANDIAMO A CREARE LA CONNESSIONE E INIZIALIZZA IL SINGLETON----
    public ConnessioneJDBC() throws ConnectionException {
        openConnection();
        initializeInstance(this);
    }


    //----METODO PER CHIUDERE LO STATEMENT----
    public void close(PreparedStatement stm) throws ConnectionException{
        try {
            if (stm != null){
                stm.close();
            }
        } catch (SQLException e){
            throw new ConnectionException("Errore nella chiusura dello statement");
        }
    }
    @Override
    //----METODO PER CREARE LA CONNESSIONE----
    protected void openConnection() throws ConnectionException{
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("connecting_info.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String user = properties.getProperty("db.user");
            String pssw = properties.getProperty("db.password");
            String dburl = properties.getProperty("db.url");
            this.connect = DriverManager.getConnection(dburl, user, pssw);
        } catch (IOException e){
            throw new ConnectionException("Errore durante la lettura del file 'connecting_info.properties'");
        } catch (SQLException e){
            throw new ConnectionException("Errore durante la connessione al database");
        }
    }

    //----METODO PER OTTENERE LA CONNESSIONE----
    public Connection getConnect() throws ConnectionException {
        try {
            if (this.connect == null || this.connect.isClosed()) {
                openConnection(); //Ricrea la connessione
            }
        } catch (SQLException e){
            throw new ConnectionException("Errore durante il controllo dello stato della connessione");
        }
        return this.connect;
    }

    //----METODO PER CHIUDERE LA CONNESSIONE----
    @Override
    public void closeConnection() throws ConnectionException{
        if (this.connect != null) {
            try {
                this.connect.close();
                this.connect = null;
            } catch (SQLException e) {
                throw new ConnectionException("Errore durante la chiusura della connessione al db");
            }
        }
    }
}
