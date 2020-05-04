package se.liu.ida.jonfa001.tddd78.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

/**
 * Klassen som visuellt representerar brädet, samt håller koll på timern för .tick funktion och även den gradvisa uppsnabbning
 * Har även hand om input från användaren och skickar vidare det till brädet
 */
public class TetrisComponent extends JComponent implements BoardListener
{
    private Board gameBoard;
    private ScoreDisplay scoreDisplay;
    private EnumMap<SquareType, Color> squareColor = new EnumMap<>(SquareType.class);
    private final int timerStartSpeed = 1000;
    private final int speedUpTime = 12000;
    private Timer tickTimer, speedUpTimer;
    private final ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("logo.png"));

    public TetrisComponent(Board newBoard) {
        newBoard.showLogo();

	this.gameBoard = newBoard;
	gameBoard.addBoardListener(this);
	loadEnumColor();

	final InputMap in = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	in.put(KeyStroke.getKeyStroke("LEFT"), "move_left");

	final ActionMap act = getActionMap();
	act.put("move_left", new FallingMoveX(-1));
	in.put(KeyStroke.getKeyStroke("RIGHT"), "move_right");
	act.put("move_right", new FallingMoveX(1));
	in.put(KeyStroke.getKeyStroke("UP"), "rotate_right");
	act.put("rotate_right", new FallingRotate(true));
	in.put(KeyStroke.getKeyStroke("DOWN"), "rotate_left");
	act.put("rotate_left", new FallingRotate(false));
	in.put(KeyStroke.getKeyStroke("P"), "toggle_pause");
	act.put("toggle_pause", new TogglePause());

	ActionListener tickPerformer = new ActionListener()
	{
	    public void actionPerformed(ActionEvent evt) {
		gameBoard.tick();
	    }
	};

	tickTimer = new Timer(timerStartSpeed, tickPerformer);
	tickTimer.setCoalesce(true);
	tickTimer.start();

	ActionListener speedUpPerformer = new ActionListener()
	{
	    public void actionPerformed(ActionEvent evt) {
	        if (tickTimer.getDelay() > 100) {
		    tickTimer.setDelay(tickTimer.getDelay() - 50);
		}
	    }
	};

	speedUpTimer = new Timer(speedUpTime, speedUpPerformer);
	speedUpTimer.setCoalesce(true);
	speedUpTimer.start();
    }

    private class FallingMoveX extends AbstractAction{
        private int xMove;
	private FallingMoveX(final int xMove) {
	    this.xMove = xMove;
	}

	@Override public void actionPerformed(final ActionEvent e) {
	    gameBoard.moveFallingBlockX(xMove);
	}
    }

    private class FallingRotate extends AbstractAction{
	private boolean rotate;
	private FallingRotate(final boolean rotate) {
	    this.rotate = rotate;
	}

	@Override public void actionPerformed(final ActionEvent e) {
	    gameBoard.rotateFallingBlock(rotate);
	}
    }
    private class TogglePause extends AbstractAction{
	@Override public void actionPerformed(final ActionEvent e) {
	    gameBoard.togglePause();
	}
    }

    public Dimension getPreferredSize() {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int boardHeight = gameBoard.getHeight();
	int squareSize = screenSize.height/boardHeight;
	int width = (gameBoard.getWidth()-1)*squareSize+20;
	int height = (int)(0.9*screenSize.height);
	return new Dimension(width, height);
    }

    private void loadEnumColor(){
	squareColor.put(SquareType.EMPTY, Color.BLACK);
	squareColor.put(SquareType.I, Color.CYAN);
	squareColor.put(SquareType.O, Color.YELLOW);
	squareColor.put(SquareType.T, Color.MAGENTA);
	squareColor.put(SquareType.S, Color.GREEN);
	squareColor.put(SquareType.Z, Color.RED);
	squareColor.put(SquareType.J, Color.BLUE);
	squareColor.put(SquareType.L, Color.ORANGE);
    }

    public void setScoreDisplay(final ScoreDisplay scoreDisplay) {
	this.scoreDisplay = scoreDisplay;
    }

    public ScoreDisplay getScoreDisplay() {
	return scoreDisplay;
    }

    public Board getGameBoard() {
	return gameBoard;
    }

    @Override protected void paintComponent(Graphics g) {
	super.paintComponent(g);

	int height = (int)this.getSize().getHeight();
	int width = (int)this.getSize().getWidth();
	final Graphics2D backGround = (Graphics2D) g;
	backGround.setColor(Color.BLACK);
	backGround.fillRect(0,0, width, height);
	if(gameBoard != null) {
	    if (gameBoard.getGameStatus() == Board.GameState.LOGO){
		int iconWidth = icon.getIconWidth();
		int iconHeight = icon.getIconHeight();
		icon.paintIcon(this, g, width / 2 - iconWidth / 2, height / 2 - iconHeight / 2);
	    } else {
		int boardHeight = gameBoard.getHeight();
		int padding = 5;
		int squareSize = (height - padding) / boardHeight;
		for (int i = 0; i < boardHeight; i++) {
		    for (int j = 0; j < gameBoard.getWidth(); j++) {
			Color blockColor = squareColor.get(gameBoard.getSquareAt(j + 2, i + 2));
			final Graphics2D squareObj = (Graphics2D) g;
			squareObj.setColor(Color.WHITE);
			squareObj.drawRect(padding + j * squareSize, padding + squareSize * i, squareSize, squareSize);
			squareObj.setColor(blockColor);
			squareObj.fillRect(padding + j * squareSize + 1, padding + squareSize * i + 1, squareSize - 1, squareSize - 1);
		    }
		}
	    }
	}
    }

    private void newGame(){
	scoreDisplay.newHighscore(gameBoard.getHighscore());
	Board newBoard = new Board(gameBoard.getWidth(), gameBoard.getHeight(), false);
	newBoard.addBoardListener(this);
	gameBoard = newBoard;
	tickTimer.setDelay(timerStartSpeed);
	tickTimer.restart();
	speedUpTimer.restart();
	gameBoard.startGame();
    }

    @Override public void boardChanged() {
	if(gameBoard.getGameStatus() == Board.GameState.FINISHED){
	    newGame();
	}
	repaint();
	scoreDisplay.repaint();
    }
}
