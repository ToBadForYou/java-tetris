package se.liu.ida.jonfa001.tddd78.tetris;

import java.util.Comparator;

/**
 * Klass för sortering av poängen
 */
public class ScoreComparator implements Comparator<Highscore>
{
    public int compare(Highscore high1, Highscore high2) {
        int score1 = high1.getScore();
        int score2 = high2.getScore();
        if (score1 > score2) return -1;
        else if (score1 == score2) return 0;
        else return 1;
    }
}
