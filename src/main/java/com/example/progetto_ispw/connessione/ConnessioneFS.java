package com.example.progetto_ispw.connessione;

import com.example.progetto_ispw.exception.ConnectionException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

//----CLASSE PER LA CONNESSIONE AL FILESYSTEM----
public class ConnessioneFS extends PersistenceConnectionManager {
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private Path filePath;
    private boolean isWritable = false; // Flag per indicare se siamo in modalità scrittura

    //----COSTRUTTORE IN CUI ANDIAMO A CREARE LA CONNESSIONE E INIZIALIZZA IL SINGLETON----
    public ConnessioneFS(String fileName) throws ConnectionException {

        File file = new File(fileName);

        this.filePath = file.toPath(); // Crea il Path dal URI

        openConnection(); //Apre la connessione al file
        initializeInstance(this); //Inizializza il singleton
    }

    @Override
    //----METODO PER APRIRE LA CONNESSIONE (APRE IL FILE IN MODALITÀ LETTURA E SCRITTURA)----
    protected void openConnection() throws ConnectionException {
        try {
            if (isWritable) {
                //Se siamo in modalità scrittura, non permettiamo lettura
                writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                //Se siamo in modalità lettura, non permettiamo scrittura
                if (Files.exists(filePath)) {
                    reader = Files.newBufferedReader(filePath);
                } else {
                    throw new ConnectionException("Il file non esiste per la lettura");
                }
            }
        } catch (IOException e) {
            throw new ConnectionException("Errore durante l'apertura del file");
        }
    }
    //----METODO PER SETTARE IL READER----
    public void setReader() throws ConnectionException {
        try {
            closeWriter();
            this.reader = Files.newBufferedReader(filePath);
        } catch (IOException e) {
            throw new ConnectionException("Errore durante l'apertura del file");
        }
    }

    //----METODO PER OTTENERE IL READER----
    public BufferedReader getReader() throws ConnectionException {
        if (reader == null) {
            throw new ConnectionException("Il reader non è stato inizializzato correttamente");
        }
        return reader;
    }
    //----METODO PER SETTARE IL WRITER---
    public void setWriter(boolean isAppend) throws ConnectionException {
        try {
            closeReader();
            isWritable = true; //Imposta la modalità scrittura
            writer = Files.newBufferedWriter(filePath,
                    StandardOpenOption.CREATE,
                    isAppend ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ConnectionException("Errore durante la creazione del writer per il file CSV");
        }
    }
    //----METODO PER OTTENERE IL WRITER----
    public BufferedWriter getWriter() throws ConnectionException {
        if (writer == null) {
            throw new ConnectionException("Il writer non è stato inizializzato correttamente");
        }
        return writer;
    }
    @Override
    //----METODO PER CHIUDERE LA CONNESSIONE (CHIUDE IL READER O WRITER)----
    public void closeConnection() throws ConnectionException {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            throw new ConnectionException("Errore durante la chiusura della connessione al file");
        }
    }

    //----METODO PER CHIUDERE IL WRITER IN MODO SICURO----
    public void closeWriter() throws ConnectionException {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                throw new ConnectionException("Errore nella chiusura del writer");
            }
        }
    }

    //----METODO PER CHIUDERE IL READER IN MODO SICURO----
    public void closeReader() throws ConnectionException {
        if (reader != null) {
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                throw new ConnectionException("Errore nella chiusura del reader");
            }
        }
    }
}
