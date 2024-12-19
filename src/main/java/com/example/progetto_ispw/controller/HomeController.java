package com.example.progetto_ispw.controller;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.dao.jdbc.CorsoDAOJDBC;
import com.example.progetto_ispw.exception.DataAccessException;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.Corso;

import java.util.ArrayList;
import java.util.List;

//----CONTROLLER APPLICATIVO PER GESTIRE LA HOME----
public class HomeController {
    //----METODO PER RICERCARE QUALI SONO I CORSI A CUI L'UTENTE È ISCRITTO----
    public List<CorsoInfoBean> getCorsiFrequentati(UtenteInfoBean utenteInfoBean){
        CorsoDAOJDBC db = new CorsoDAOJDBC();
        List<CorsoInfoBean> corsoInfoBeans = new ArrayList<>();
        try {
            List<Corso> corsiFrequentati = db.getCourses(utenteInfoBean);
            corsiFrequentati.forEach(corso -> {
                CorsoInfoBean corsoInfoBean = new CorsoInfoBean(corso.getNome(), corso.getDescrizione());
                corsoInfoBeans.add(corsoInfoBean);
            });
        } catch (DataNotFoundException | DataAccessException e){
            CorsoInfoBean corsoInfoBean = new CorsoInfoBean(e.getMessage(), "");
            corsoInfoBeans.add(corsoInfoBean);
        }
        return corsoInfoBeans;
    }
}
