package com.example.progetto_ispw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

//----CLASSE PER LA CONNESSIONE AL DB (IMPLEMENTA IL PATTERN SINGLETON)----
public class Connessione {
    private static Connessione istance; //----VARIABILE STATICA CHE CONTIENE L'UNICA ISTANZA DELLA CLASSE
    private Connection connect; //----CONNESSIONE AL DB

    //----COSTRUTTORE PRIVATO PER IMPEDIRE L'ISTANZAZIONE ESTERNA----
    private Connessione() throws SQLException {
        createConnection();
    }

    //----METODO STATICO PER OTTENERE L'ISTANZA UNICA----
    public static synchronized Connessione getInstance() throws SQLException {
        if(istance == null){
            istance = new Connessione();
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
            System.out.println("Errore nella chiusura dello statement");
        }
    }
    //----METODO PER CREARE LA CONNESSIONE----
    private void createConnection() throws SQLException {
        if (connect == null || connect.isClosed()){
            try{
                InputStream input = getClass().getClassLoader().getResourceAsStream("connecting_info.properties");
                Properties properties = new Properties();
                properties.load(input);

                String user = properties.getProperty("db.user");
                String pssw = properties.getProperty("db.password");
                String dburl = properties.getProperty("db.url");
                if(connect == null || connect.isClosed()) {
                    connect = DriverManager.getConnection(dburl, user, pssw);
                }
            } catch (FileNotFoundException e){
                System.out.println("File inesistente o non accessibile");
            } catch (IOException e){
                System.out.println("Errore durante la lettura del file 'connecting_info.properties'");
            } catch (SQLException e){
                System.out.println("Errore durante la connessione al database");
            }
        }
    }

    //----METODO PER OTTENERE LA CONNESSIONE----
    public Connection getConnect() throws SQLException {
        if (connect == null || connect.isClosed()){
            createConnection(); //Ricrea la connessione
        }
        return connect;
    }

    //----METODO PER CHIUDERE LA CONNESSIONE----
    public void closeConnection() {
        if (connect != null) {
            try {
                connect.close();
                connect = null;
            } catch (SQLException e) {
                System.out.println("Errore durante la chiusura della connessione al db");
            }
        }
    }
}
