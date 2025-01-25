package com.example.progetto_ispw.dao;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.NotificheInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.exception.ConnectionException;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.exception.UpdateDataException;
import com.example.progetto_ispw.model.Corso;

import java.util.List;

//----INTERFACCIA PER DEFINIRE LA BASE PER OGNI CLASSE DAO CHE DEVE OCCUPARSI DEI CORSI----
public interface CorsoDAO extends DAO{
    List<Corso> getCourses(UtenteInfoBean user) throws DataNotFoundException, DataAccessException, ConnectionException; //Metodo per ottente tutti i corsi a cui è iscritto l'utente
    NotificheInfoBean getNotifiche(UtenteInfoBean utente, CorsoInfoBean corso) throws DataAccessException; //Metodo per prelevare le notifiche del tutor del corso
    void updateNotifiche(UtenteInfoBean utente, CorsoInfoBean corso, QuizInfoBean quiz) throws DataAccessException, UpdateDataException; //Metodo per mandare le notifiche al tutor del corso
}
