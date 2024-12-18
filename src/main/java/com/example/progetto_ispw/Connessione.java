package com.example.progetto_ispw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

//----CLASSE PER LA CONNESSIONE AL DB (IMPLEMENTA IL PATTERN SINGLETON)----
public class Connessione {
    private static Connessione istance = null; //----VARIABILE STATICA CHE CONTIENE L'UNICA ISTANZA DELLA CLASSE
    private Connection connect = null; //----CONNESSIONE AL DB

    private Logger logger; //----VARIABILE LOGGER PER LA GESTIONE DELLE ECCEZIONI PREDEFINITE----

    //----COSTRUTTORE PRIVATO PER IMPEDIRE L'ISTANZAZIONE ESTERNA----
    protected Connessione() {
        createConnection();
    }

    //----METODO STATICO PER OTTENERE L'ISTANZA UNICA----
    public static synchronized Connessione getInstance() {
        if(Connessione.istance == null){
            Connessione.istance = new Connessione();
        }
        return istance;
    }

    //----METODO PER CHIUDERE LO STATEMENT----
    public void close(PreparedStatement stm) {
        try {
            if (stm != null){
                stm.close();
            }
        } catch (SQLException e){
            this.logger.severe("Errore nella chiusura dello statement");
        }
    }
    //----METODO PER CREARE LA CONNESSIONE----
    private void createConnection() {
        try{
            this.logger = Logger.getLogger(getClass().getName());
            InputStream input = getClass().getClassLoader().getResourceAsStream("connecting_info.properties");
            Properties properties = new Properties();
            properties.load(input);

            String user = properties.getProperty("db.user");
            String pssw = properties.getProperty("db.password");
            String dburl = properties.getProperty("db.url");
            this.connect = DriverManager.getConnection(dburl, user, pssw);
        } catch (FileNotFoundException e){
            this.logger.severe("File inesistente o non accessibile");
        } catch (IOException e){
            this.logger.severe("Errore durante la lettura del file 'connecting_info.properties'");
        } catch (SQLException e){
            this.logger.severe("Errore durante la connessione al database");
        }
    }

    //----METODO PER OTTENERE LA CONNESSIONE----
    public Connection getConnect() {
        try {
            if (this.connect == null || this.connect.isClosed()) {
                createConnection(); //Ricrea la connessione
            }
        } catch (SQLException e){
            this.logger.severe("Errore durante il controllo dello stato della connessione");
        }
        return this.connect;
    }

    //----METODO PER CHIUDERE LA CONNESSIONE----
    public void closeConnection() {
        if (this.connect != null) {
            try {
                this.connect.close();
                this.connect = null;
            } catch (SQLException e) {
                this.logger.severe("Errore durante la chiusura della connessione al db");
            }
        }
    }
}
