package se.liu.ida.jonfa001.tddd78.tetris;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Klassen som visar nuvarande poäng, powerups och samt highscore listan
 */
public class ScoreDisplay extends JComponent
{
    private HighscoreList highScoreList;
    private TetrisComponent parent;

    public ScoreDisplay(TetrisComponent parent) {
        this.parent = parent;
	this.highScoreList = new HighscoreList();
	highScoreList.loadHighscores();
    }

    public Dimension getPreferredSize() {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = 210;
	int height = (int)(0.9*screenSize.height);
	return new Dimension(width, height);
    }

    public void newHighscore(Highscore newHighscore) {
	try {
	    highScoreList.addHighscore(newHighscore);
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(this,
					  e.toString(),
					  "Saving Error",
					  JOptionPane.WARNING_MESSAGE);
	}

    }

    @Override protected void paintComponent(Graphics g) {
	super.paintComponent(g);

	if(parent.getGameBoard() != null) {
	    g.setColor(Color.BLACK);
	    g.fillRect(5,5,200, 75);
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("serif", Font.PLAIN, 30));
	    String score = "Score: " + parent.getGameBoard().getScore();
	    g.drawString(score, 10, 50);

	    g.setColor(Color.BLACK);
	    g.fillRect(5,85,200, 40);
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("serif", Font.PLAIN, 20));
	    String powerup = parent.getGameBoard().getFallType();
	    g.drawString("Powerup: " + powerup, 10, 110);

	    int highScoreHeight = 10*25 + 60;
	    int highScoreY = getHeight() - highScoreHeight- 5;
	    g.setColor(Color.BLACK);
	    g.fillRect(5,highScoreY,200, highScoreHeight);
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("serif", Font.PLAIN, 30));
	    g.drawString("Highscores", 10, highScoreY+30);
	    g.setFont(new Font("serif", Font.PLAIN, 20));
	    int counter = 0;
	    for (Highscore highScore: highScoreList.getHighscores()) {
		g.drawString(highScore.toString(), 10, highScoreY+60+25*counter);
		counter += 1;
		if(counter >= 10){
		    break;
		}
	    }
	}
    }
}
