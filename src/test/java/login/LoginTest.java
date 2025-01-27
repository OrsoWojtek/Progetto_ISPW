package login;

import com.example.progetto_ispw.bean.LoginInfoBean;
import com.example.progetto_ispw.controller.LoginController;
import com.example.progetto_ispw.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
/*  Autore: Alessandro Brunori
 *  Matricola: 0311012
 */

/*
 *  Il test si occupa di verificare se il processo di login avvenga correttamente
 */
class LoginTest{

        @Test
        void loginTest() throws Exception {
                int flag = 0;
                LoginInfoBean userBean = new LoginInfoBean();
                userBean.setUsername("a");
                userBean.setPassword("s");
                LoginController controller = new LoginController();
                try{
                        controller.checkLogin(userBean);
                } catch (DataNotFoundException e){ //L'utente non è stato trovato
                        flag = 1;
                }
                assertEquals(0,flag);
        }
}
