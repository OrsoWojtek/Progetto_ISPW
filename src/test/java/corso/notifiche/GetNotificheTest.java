package corso.notifiche;

import com.example.progetto_ispw.bean.CorsoInfoBean;
import com.example.progetto_ispw.bean.UtenteInfoBean;
import com.example.progetto_ispw.constants.DataID;
import com.example.progetto_ispw.constants.SessionID;
import com.example.progetto_ispw.controller.CorsoPageController;
import com.example.progetto_ispw.exception.DataNotFoundException;
import com.example.progetto_ispw.model.sessione.SessionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*  Autore: Alessandro Brunori
 *  Matricola: 0311012
 */

/*
 *  Il test si occupa di verificare l'esistenza del recupero delle notifiche avvenga correttamente
 */
class GetNotificheTest {
    @BeforeAll
    static void setUp(){
        UtenteInfoBean utente = new UtenteInfoBean();
        utente.setUsername("totti");

        //Creaiamo la sessione 'login' contenente le info sull'utente
        SessionManager.getInstance().createSession(SessionID.LOGIN).addDato(DataID.UTENTE,utente);

        CorsoInfoBean corso = new CorsoInfoBean();
        corso.setNome("ISPW");

        //Creaiamo la sessione per la pagina del corso contenente le info sul corso corrente
        SessionManager.getInstance().createSession(SessionID.COURSE_PAGE).addDato(DataID.CORSO,corso);
    }

    @Test
    void getInfoNotificheTest() throws Exception{
        int flag = 0;
        CorsoPageController controller = new CorsoPageController();
        try {
            controller.getInfoNotifiche();
        } catch (DataNotFoundException e) {
            flag = 1;
        }
        assertEquals(0,flag);
    }

}
