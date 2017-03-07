package quizzapp;

import java.util.List;

/**
 *
 * @author Lydya0103
 */
public class Game {
    private static final int QUESTION_NUMBER = 10;
    private static final Utiles utils = new Utiles();
    
    private List<Question> Questions;
    
    private String UserAnswerForQuestion;
    private boolean StartGame;
    private boolean UserAnswered;
    private float PlayerScore;
    private String PlayeName;
    private int CorrectAnswers;
    
    public void init()
    {        
        utils.loadQuestionFiles();
        if(utils.areQuestions() == false)
        {
            //aici e problema ca nu am intrebari in foldere            
        }
        utils.readScoreFile();
        utils.loadScoreTable();
        //aici cred ca mai trebuie sa fac si lucruri legate de GUI
        Questions = utils.pickQuestions();
    }
    
    public void beginGame()
    {
        while(true)
        {
            if(StartGame == true)
            {
                // aici sa ii arate un panou gen be ready
                for(Question question : Questions)
                {
                    putUserQuestion(question);
                }
                PlayerScore = (10 * (float) QUESTION_NUMBER) / (float) CorrectAnswers;
                if(utils.isInTop(PlayerScore))
                {
                    // sa afiseze ca e in top
                    // astept sa imi dea numele, vad eu cum
                    utils.addScore(PlayeName, PlayerScore);
                    // mai redesenez odata tabela de scor, sa apara si el
                    // astept sa dea pe butonul de meniu
                }
                else
                {
                    // ii dau un panou sa mai incerce ,ptr cateva sec (eu stabilesc)
                    // ii afisez meniul
                }
                resetPlayer();                
            }
            else
            {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {}
            }
        }
    }
    
    /**
     * Does everything for current question shown.
     * @param question current shown question on the screen.
     */
    private void putUserQuestion(Question question)
    {
        // ii trimit intrebarea gui ului
        //ii dau repaint cred
        waitForUserAnswer(question.getTime());
        if(question.getCorrectAnswer().equals(UserAnswerForQuestion) == true)
        {
            CorrectAnswers++;
            //sa ii arate si ca a respuns corect, cred
        }
        else
        {
            //aici GUI ul trebuie sa ii arate ce a gresit etc..
        }
    }
    
    /**
     * Wait for an answer from GUI (UserAnswered to be true). Counts each second
     * and tell to GUI to repaint the timer.
     * Ends when user answer or time is elapsed.
     * @param QuestionTime is the current question`s time for answer.
     */
    private void waitForUserAnswer(int QuestionTime)
    {
        long questionTime = QuestionTime * 1000;
        long startQuestionTime = System.currentTimeMillis();
        long lastTime = startQuestionTime;
        while(UserAnswered == false)
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {}
            if(System.currentTimeMillis() - lastTime > 980)
            {
                //aici a trecut o secunda, ii zic gui ului sa modifice timmerul + repain
            }
            if(System.currentTimeMillis() - startQuestionTime > questionTime)
                break;
        }
        UserAnswered = false;
    }
    
    /**
     * reset all variables for player who just finished the test.
     */
    private void resetPlayer()
    {
        PlayerScore = 0;
        PlayeName = "";
        CorrectAnswers = 0;
        StartGame = false;
    }
    
    public void sendAnswer(String answer)
    {
        UserAnswered = true;
        UserAnswerForQuestion = answer;        
    }
    
    public void startGame()
    {
        StartGame = true;
    }
}
