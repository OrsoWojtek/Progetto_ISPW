package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.constants.DataID;
import com.example.progetto_ispw.constants.SessionID;
import com.example.progetto_ispw.dao.jdbc.CorsoDAOJDBC;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.Corso;
import com.example.progetto_ispw.model.sessione.SessionManager;

import java.util.ArrayList;
import java.util.List;

//----CONTROLLER APPLICATIVO PER GESTIRE LA HOME----
public class HomeController {

    //----METODO PER RICERCARE QUALI SONO I CORSI A CUI L'UTENTE È ISCRITTO----
    public List<CorsoInfoBean> getCorsiFrequentati(UtenteInfoBean utenteInfoBean) throws ConnectionException {
        CorsoDAOJDBC db = new CorsoDAOJDBC();
        List<CorsoInfoBean> catalogoBeans = new ArrayList<>();
        try {
            List<Corso> corsiFrequentati = db.getCourses(utenteInfoBean);
            corsiFrequentati.forEach(corso -> {
                SessionManager.getInstance().createSession(SessionID.CATALOGO_CORSI).addEntity(corso.getNome(),corso); //Creazione di una sessione dedicata per contenere il catalogo dei corsi estratti dal db
                CorsoInfoBean corsoInfoBean = new CorsoInfoBean();
                corsoInfoBean.setNome(corso.getNome());
                corsoInfoBean.setDescrizione(corso.getDescrizione());
                catalogoBeans.add(corsoInfoBean);
            });
        } catch (DataNotFoundException | DataAccessException e){
            CorsoInfoBean corsoInfoBean = new CorsoInfoBean();
            corsoInfoBean.setNome(e.getMessage());
            corsoInfoBean.setDescrizione("");
            catalogoBeans.add(corsoInfoBean);
        }
        return catalogoBeans;
    }
    //----METODO PER PULIRE CONNESSIONE AL DB E SESSIONE----
    public void clean() throws ConnectionException {
        SessionManager.getInstance().invalidateSessions(); //Formatto le sessioni
        Connessione conn = Connessione.getInstance();
        conn.closeConnection(); //Chiudo definitivamente la connessione con il db
    }
    //----METODO PER OTTENERE LE INFORMAZIONI DELL'UTENTE CORRENTE----
    public UtenteInfoBean getInfoUser() throws DataNotFoundException {
        return SessionManager.getInstance().getSession(SessionID.LOGIN).getDato(DataID.UTENTE,UtenteInfoBean.class);
    }
    //----METODO PER MEMORIZZARE NELLA SESSIONE IL CORSO SELEZIONATO
    public void setInfoCourse(CorsoInfoBean currentCourse) throws DataNotFoundException {
        SessionManager istance = SessionManager.getInstance();
        Corso corsoSelezionato = istance.getSession(SessionID.CATALOGO_CORSI).getEntity(currentCourse.getNome(), Corso.class);
        istance.createSession(SessionID.COURSE_PAGE).addDato(DataID.CORSO,currentCourse);
        istance.getSession(SessionID.COURSE_PAGE).addEntity(DataID.CORSO.getValue(), corsoSelezionato);
    }
}
