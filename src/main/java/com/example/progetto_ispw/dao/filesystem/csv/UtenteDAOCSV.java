package com.example.progetto_ispw.dao.filesystem.csv;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.constants.UserRole;
import com.example.progetto_ispw.dao.UtenteDAO;
import com.example.progetto_ispw.dao.filesystem.DAOFS;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.RoleNotFoundException;
import com.example.progetto_ispw.model.Utente;
import com.example.progetto_ispw.pattern.factory.UtenteFactory;

import java.io.IOException;

public class UtenteDAOCSV extends DAOFS implements UtenteDAO {
    public UtenteDAOCSV() throws ConnectionException {
        String fileName = "file/utenti.csv";
        getConnessione(fileName,false);
    }
    //----METODO PER VERIFICARE LA PRESENZA DELLE CREDENZIALI INSERITE NEL CSV----
    @Override
    public Utente getNewUtente(LoginInfoBean currentCred) throws DataNotFoundException, DataAccessException, RoleNotFoundException, ConnectionException {
        String ruolo = null;
        Utente utente = null;

        try {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                String[] parts = line.split(",");  //Supponiamo che i dati siano separati da virgole
                String username = parts[0];
                String password = parts[1];
                ruolo = parts[2];
                if(ruolo.equalsIgnoreCase("T")){
                    ruolo = UserRole.TUTOR.getValue();
                }
                if(ruolo.equalsIgnoreCase("S")){
                    ruolo = UserRole.STUDENTE.getValue();
                }
                //Verifica se le credenziali corrispondono
                if (username.equals(currentCred.getUsername()) && password.equals(currentCred.getPassword())) {
                    //Se trovata la corrispondenza, crea un oggetto Utente
                    utente = UtenteFactory.getFactory(ruolo).createUtente(username, password);
                    break;  //Esci dal ciclo una volta trovato l'utente
                }
            }

            if (utente == null) {
                throw new DataNotFoundException("Username e/o password inseriti non corretti o non registrati. Prego riprovare.");
            }

            //Verifica il ruolo dell'utente
            if (ruolo == null) {
                throw new RoleNotFoundException("Ruolo non trovato per l'utente.");
            }

            //Restituisce l'utente trovato
            return utente;

        } catch (IOException e) {
            throw new DataAccessException("Errore durante l'accesso ai dati del file.");
        } finally {
            //Chiudi la connessione al file dopo aver completato la lettura
            closeConnection();
        }
    }


}
