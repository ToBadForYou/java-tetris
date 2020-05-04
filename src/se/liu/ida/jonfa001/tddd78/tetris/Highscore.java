package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Klass för highscores
 */
public class Highscore
{
    private String name;
    private int score;

    public Highscore() {
    }

    public int getScore() {
	return score;
    }

    public void setHighscore(String name, int score){
        this.name = name;
        this.score = score;
    }

    @Override public String toString() {
	return name + ": " + score;
    }
}
