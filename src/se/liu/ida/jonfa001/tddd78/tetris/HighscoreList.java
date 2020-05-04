package se.liu.ida.jonfa001.tddd78.tetris;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * En klass som sparar alla highscores i en lista
 */
public class HighscoreList
{
    private List<Highscore> highscores;

    public HighscoreList() {
    }

    public List<Highscore> getHighscores() {
        return highscores;
    }

    /**
     * Laddar highscore listan från .txt fil
     */
    public void loadHighscores(){
        Gson gson = new Gson();
        List<Highscore> highScorelist = null;
        String highscorePath = System.getProperty("user.dir") + "/highscores.txt";
        File highscore = new File(highscorePath);

        if (highscore.exists()){ // Här kollar vi om filen existerar, om den ej existerar kommer den inte heller fortsätta
                                 // därför behöver vi ej skicka vidare erroret då det i praktiken aldrig kommer hända
            try {
                File myObj = new File(highscorePath);
                Scanner myReader = new Scanner(myObj);
                if (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    highScorelist = gson.fromJson(data, new TypeToken<List<Highscore>>(){}.getType());
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
        if (highScorelist != null) {
            highscores = highScorelist;
            sortHighscores();
        } else {
            highscores = new ArrayList<>();
	}
    }

    /**
     * Sparar highscore listan till .txt fil
     * @throws IOException
     */
    private void saveHighscores() throws IOException{
        String highscorePath = System.getProperty("user.dir") + "/highscores.txt";
        File highscore = new File(highscorePath);
        Gson gson = new Gson();
        String listAsJson = gson.toJson(highscores);

       /* if (!highscore.exists() && highscore.createNewFile()){
            FileWriter writer = new FileWriter(highscore);
            writer.write(listAsJson);
            writer.close();
        } else {*/

       String tempHighscorePath = System.getProperty("user.dir") + "/highscores_temp.txt";
       File tempHighscore = new File(tempHighscorePath);
       if (tempHighscore.exists()) {
           tempHighscore.delete(); // If temp file exists, remove it
       }
       if (tempHighscore.createNewFile()) {
           FileWriter writer = new FileWriter(tempHighscore);
           writer.write(listAsJson);
           writer.close();

           highscore.delete();
           File newName = new File(highscorePath);
           tempHighscore.renameTo(newName);
       } else throw new IOException("Failed to save highscore.");
    }

    /**
     * Sorterar highscore listan med hjälp av ScoreComparator klassen
     */
    public void sortHighscores(){
        highscores.sort(new ScoreComparator());
    }

    /**
     * Lägger till ett highscore i listan
     * @param newHighscore
     * @throws IOException
     */
    public void addHighscore(Highscore newHighscore) throws IOException {
        highscores.add(newHighscore);
        sortHighscores();
        saveHighscores();
    }
}
