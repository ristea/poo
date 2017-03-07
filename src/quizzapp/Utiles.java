package quizzapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Lydya0103
 */
public class Utiles {
    private static final int NUMBER_TOP_PLAYERS = 10;
    private static final int NUMBER_OF_QUESTIONS = 10;
    
    private static final String FILE_NAME = "resources" + File.separator + "Ierarhie.properties";    
    private static final String[] PATHS = {"resources" + File.separator + "PC.txt",
        "resources" + File.separator + "SDA.txt",
        "resources" + File.separator + "POO.txt",
        "resources" + File.separator + "CID.txt"};
    
    private final List<Question> AllQuestions = new LinkedList<>();    
    private final LinkedList<String> ScoreTable = new LinkedList<>();
    private final Properties props = new Properties();
    private BufferedReader bufferReader;
    //private String[] result;
    
    /**
     * Load all questions from files(PC/SDA/POO/CID) into a LinkedList
     * and shuffle all question for a random pick.
     */
    public void loadQuestionFiles()
    {
        for(int i = 0; i < PATHS.length; i++)
        {
            try {
                bufferReader = new BufferedReader(new FileReader(PATHS[i]));
                while(bufferReader.ready() == true)
                {
                    String currentLine = bufferReader.readLine();
                    currentLine = currentLine.trim();
                    if(currentLine.startsWith("#"))
                        continue;
                    String[] splitLine;
                    splitLine = currentLine.split("\\$");
                    
                    if(splitLine[0].split("\\%").length > 1)                   
                        splitLine[0] = splitLine[0].replaceAll("\\%", System.lineSeparator());                  
                    
                    try
                    {
                        AllQuestions.add( new Question(splitLine[0], Arrays.copyOfRange(splitLine,1,5), 
                            Integer.parseInt(splitLine[5].trim())) );
                    }
                    catch(Exception ex) {}
                }           
            } catch (IOException ex) {}            
        }            
        Collections.shuffle(AllQuestions);
    }
    
    /**
     * @return a shuffled List with Question, just ready to be shown.
     */
    public List<Question> pickQuestions()
    {
        List<Question> randomQuestions = new LinkedList<>(AllQuestions);
        Collections.shuffle(randomQuestions);
        return NUMBER_OF_QUESTIONS > randomQuestions.size() ? 
                AllQuestions : randomQuestions.subList(0, NUMBER_OF_QUESTIONS);
    }
    
    /**
     * @return TRUE if there are some questions in LinkedList and FALSE
     * if there are not.
     */
    public boolean areQuestions()
    {
        return !AllQuestions.isEmpty();
    }
    
    public void readScoreFile() {
        FileInputStream in;
        try {
            in = new FileInputStream(FILE_NAME);
            props.load(in);
            in.close();
        } catch (FileNotFoundException ex) {
            File file = new File(FILE_NAME);
            try {
                file.createNewFile();
            } catch (IOException ex1) {}
        } catch (IOException ex) {}
    }
    
    /**
     * Load ScoreTable hold into a properties file to a LinkedList.
     */
    public void loadScoreTable()
    {
        if(props.isEmpty() == true)
            return;
        Enumeration enuKeys = props.keys();
        while (enuKeys.hasMoreElements()) {
            String key = (String) enuKeys.nextElement();
            float value = Float.parseFloat(props.getProperty(key));
            if(ScoreTable.isEmpty() == true)
                ScoreTable.add(value + "@" + key);
            else
            {
                if(value > Float.parseFloat(ScoreTable.get(0).split("@")[0]))
                    ScoreTable.add(0, value + "@" + key);
                else
                    for(int i = ScoreTable.size() - 1; 0 <= i; i--)          
                        if(value < Float.parseFloat(ScoreTable.get(i).split("@")[0]))
                        {
                            ScoreTable.add(i + 1, value + "@" + key);
                            break;
                        }
            }                
        }
    }
    
    /**
     * Add the player and his score to the ScoreTable.
     * The ScoreTable remember just NUMBER_TOP_PLAYERS. 
     * @param PlayerName Player`s name.
     * @param PlayerScore Player`s score.
     */
    public void addScore(String PlayerName, float PlayerScore)
    {
        if(ScoreTable.isEmpty() == true)
        {
            ScoreTable.add(Float.toString(PlayerScore) + "@" + PlayerName);
            return;
        }            
                   
        if(ScoreTable.size() < NUMBER_TOP_PLAYERS)
        {
            if(PlayerScore >= Float.parseFloat(ScoreTable.get(0).split("@")[0]))           
                ScoreTable.add(0, Float.toString(PlayerScore) + "@" + PlayerName);                                
            else
                for(int i = ScoreTable.size() - 1; 0 <= i; i--)          
                    if(PlayerScore < Float.parseFloat(ScoreTable.get(i).split("@")[0]))
                    {
                        ScoreTable.add(i + 1, Float.toString(PlayerScore) + "@" + PlayerName);
                        return;
                    }                                          
        }       
        else
        {
            if(PlayerScore >= Float.parseFloat(ScoreTable.get(0).split("@")[0]))           
                ScoreTable.add(0, Float.toString(PlayerScore) + "@" + PlayerName);                         
            else
                for(int i = ScoreTable.size() - 2; 0 <= i; i--)          
                    if(PlayerScore < Float.parseFloat(ScoreTable.get(i).split("@")[0])) 
                    {
                        ScoreTable.add(i + 1, Float.toString(PlayerScore) + "@" + PlayerName);
                        break;
                    }                        
            ScoreTable.remove(NUMBER_TOP_PLAYERS);
        }
    }
    
    public boolean isInTop(float PlayerScore)
    {
        if(ScoreTable.isEmpty() == true)
            return true;
        if(ScoreTable.size() < NUMBER_TOP_PLAYERS)
            return true;
        if(PlayerScore > Float.parseFloat(ScoreTable.get(NUMBER_TOP_PLAYERS - 1).split("@")[0]))
            return true;
        return false;        
    }
    
    public LinkedList<String> getScoreTable()
    {
        return ScoreTable;
    }
}
