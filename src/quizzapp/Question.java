package quizzapp;

import java.util.Random;

/**
 * Format of a question in a QuestionFile.
 * QuestionText Answer1 Answer2 Answer3 Answer4 Time
 * Answer1 is correct.
 * @author Lydya0103
 */
public class Question {
    private final String QuestionText;
    private final String[] Answers;
    private final String CorrectAnswer;
    private final int Time;
    
    public Question(String _QuestionText, String[] _Answers, int _Time)
    {
        QuestionText = _QuestionText;
        Answers = _Answers;
        Time = _Time;
        CorrectAnswer = _Answers[0];
        shuffleArray(Answers);
    }
    
    public int getTime()
    {
        return Time;
    }
    
    public String getQuestionText()
    {
        return QuestionText;
    }
    
    /**
     * @return an array with all answers for this question.
     */
    public String[] getAnswers()
    {
        return Answers;
    }
    
    public String getCorrectAnswer()
    {
        return CorrectAnswer;
    }
    
    private void shuffleArray(String[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
          int index = rnd.nextInt(i + 1);
          String a = ar[index];
          ar[index] = ar[i];
          ar[i] = a;
        }
    }
    
    @Override
    public String toString()
    {
        // functie de test pentru a verifica randomizarea intrebarilor
        String s = QuestionText;
        for(int i = 0;i<4;i++)
        {
            s+= " " + Answers[i];
        }
        s += " " + Time;
        return s;
    }

    
}
