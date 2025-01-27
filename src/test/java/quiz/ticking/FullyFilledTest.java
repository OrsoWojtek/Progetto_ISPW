package quiz.ticking;

import com.example.progetto_ispw.bean.QuesitoInfoBean;
import com.example.progetto_ispw.bean.QuizInfoBean;
import com.example.progetto_ispw.bean.RispostaInfoBean;
import com.example.progetto_ispw.controller.QuizController;
import com.example.progetto_ispw.exception.NotFilledQuestionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
/*  Autore: Alessandro Brunori
 *  Matricola: 0311012
 */

/*
 *  Il test si occupa di verificare che venga restituita l'eccezione in caso di quiz non completamente compilato
 *  (ovvero che almeno un quesito del quiz non presenti alcuna risposta selezionata) e che non venga restituita in caso contrario
 */
class FullyFilledTest {
    private QuizInfoBean quiz;
    @BeforeEach
    void setUpQuiz(){
        initializeQuiz(); //Inizializzazione del quiz
    }

    private void initializeQuiz(){
        //Creazione del pool di quesiti
        List<QuesitoInfoBean> quesiti = new ArrayList<>();
        int numDiQues = 3; //Ne creo 3 (scelta arbitraria)

        for(int i = 0; i < numDiQues; i++) {

            //Creazione del pool di risposte
            List<RispostaInfoBean> risposte = new ArrayList<>();
            int numDiRisp = 4; //Ne creo 4 per rimanere coerente con il progetto

            for (int y = 0; y < numDiRisp; y++) {
                risposte.add(new RispostaInfoBean()); //Aggiunta di una nuova risposta al pool
                risposte.getLast().setTicked(false); //Setting di ogni nuova risposta aggiunta come non selezionata
            }

            QuesitoInfoBean quesito = new QuesitoInfoBean(); //Creazione del singolo quesito
            quesito.setRisposte(risposte); //Aggiunta del nuovo pool di risposte al quesito
            quesiti.add(quesito); //Aggiunta del quesito al pool di quesiti
        }
        quiz = new QuizInfoBean(); //Creazione del quiz
        quiz.setQuesiti(quesiti); //Aggiunta del pool di quesiti al quiz
    }

    //----METODO PER METTERE COME TICKED OGNI PRIMA RISPOSTA DI OGNI QUESITO DEL QUIZ---
    private void tickAnswers(){
        quiz.getQuesiti().forEach(quesito -> quesito.getRisposte().getFirst().setTicked(true));
    }

    @Test
    void notFullyFilledTest(){
        int flag = 0;
        QuizController controller = new QuizController();
        try {
            controller.isFullyFilled(quiz);
        } catch (NotFilledQuestionException e) {
            flag = 1;
        }
        assertEquals(1,flag);
    }

    @Test
    void fullyFilledTest(){
        int flag = 0;
        QuizController controller = new QuizController();
        tickAnswers();
        try {
            controller.isFullyFilled(quiz);
        } catch (NotFilledQuestionException e) {
            flag = 1;
        }
        assertEquals(0,flag);
    }
}
