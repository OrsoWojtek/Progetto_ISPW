package com.example.progetto_ispw.dao;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.UpdateDataException;
import com.example.progetto_ispw.model.Quiz;

import java.util.List;

//----INTERFACCIA PER DEFINIRE LA BASE PER OGNI CLASSE DAO CHE DEVE OCCUPARSI DEI QUIZ----
public interface QuizDAO extends DAO{
    //QuizDAO initialize(); //Metodo in cui chiamare il costruttore della classe dao concreta
    List<Quiz> getQuizzes(CorsoInfoBean corso, UtenteInfoBean utente) throws DataAccessException; //Metodo per ottente tutti i quiz presenti nel corso METODO PER OTTENERE TUTTI I QUIZ PRESENTI NEL CORSO
    void updateScore(Quiz quiz, UtenteInfoBean utente, CorsoInfoBean corso) throws DataAccessException, UpdateDataException; //Metodo per aggiornare il punteggio ottenuto dallo studente ad un quiz
}
