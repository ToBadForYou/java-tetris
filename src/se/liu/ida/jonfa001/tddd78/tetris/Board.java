package se.liu.ida.jonfa001.tddd78.tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Board klassen, har hand om huvudfunktionerna för spelets mekanik
 */
public class Board {
    enum GameState {
        NONE,
        PAUSED,
        LOGO,
        RUNNING,
        HIGHSCORE,
        FINISHED
    }

    private SquareType[][] squares;
    private int width, height, polyX, polyY, score;
    private Poly falling;
    private Random rndSquare;
    private List<BoardListener> boardListeners;
    private static final int BORDERSQUARES = 2;
    private GameState gameStatus = GameState.NONE;
    private Highscore highscore;
    private FallHandler fallHandler;
    private FallHandler nextPowerup;

    public Board(int width, int height, boolean random) {
        this.fallHandler = new DefaultFallHandler();
        this.rndSquare = new Random();
        SquareType[][] genSquares = new SquareType[height + BORDERSQUARES * 2][width + BORDERSQUARES * 2];
        if (random){
            genSquares = randomizeSquares(genSquares, width, height);
        }
        else {
            for (int i = 0; i < height + BORDERSQUARES * 2; i++) {
                for (int j = 0; j < width + BORDERSQUARES * 2; j++) {
                    if (i >= BORDERSQUARES && i < height + BORDERSQUARES && j >= BORDERSQUARES && j < width + BORDERSQUARES) {
                        genSquares[i][j] = SquareType.EMPTY;
                    }
                    else {
                        genSquares[i][j] = SquareType.OUTSIDE;
                    }
                }
            }
        }
        this.squares = genSquares;
        this.width = width;
        this.height = height;
        this.boardListeners = new ArrayList<>();
        this.nextPowerup = null;
    }

    public void startGame(){
        gameStatus = GameState.RUNNING;
    }

    public String getFallType() {
        return fallHandler.getDescription();
    }

    /**
    Togglar pause i spelet
     */
    public void togglePause(){
        if (gameStatus == GameState.RUNNING){
            gameStatus = GameState.PAUSED;
        }else if (gameStatus == GameState.PAUSED){
            gameStatus = GameState.RUNNING;
        }
    }

    /**
    Kollar om det finns någon tom ruta i y-led (nedåt) i givna kordinater x,y
     */
    private int getEmptySquareY(int x, int y){
        for (int i = y; i < height + BORDERSQUARES; i++) {
            if (getSquareType(x, i) == SquareType.EMPTY) {
                return i;
            }
        }
        return 0;
    }

    /**
    Kollar om det är möjligt att kollapsa blocksen om inte returnera false, annars kollapsa blocksen
     */
    public boolean attemptCollapseBlocks(List<Coordinates> collisionCoords){
        for (Coordinates coords: collisionCoords) {
            int emptyY = getEmptySquareY(coords.getX(), coords.getY());
            if (emptyY == 0) {
                return false;
            } else coords.setEmptyY(emptyY);
        }
        collapseBlocksY(collisionCoords);
        return true;
    }

    /**
    Kollapsar blocksen från kordinater(Coordinates objekt) i givna listan
     */
    private void collapseBlocksY(List<Coordinates> collisionCoords){
        for (Coordinates coords: collisionCoords) {
            squares[coords.getEmptyY()][coords.getX()] = getSquareType(coords.getX(), coords.getY());
            squares[coords.getY()][coords.getX()] = SquareType.EMPTY;
            checkCompleteRow(coords.getEmptyY(), 1);
        }
    }

    /**
    Visar tetris logon
     */
    public void showLogo(){
        gameStatus = GameState.LOGO;
        ActionListener taskPerformer = new ActionListener()
        {
            public void actionPerformed(ActionEvent evt) {
                gameStatus = GameState.RUNNING;
            }
        };

        final Timer clockTimer = new Timer(800, taskPerformer);
        clockTimer.setRepeats(false);
        clockTimer.start();
    }

    /**
    Lägger till poäng, tar som inargument antalet rader som fyllts
     */
    private void addScore(int rows){
        int score = 100;
        if (rows > 3){
            score = 800;
        }
        else if(rows > 1){
            score += (rows-1)*200;
        }
        this.score += score;
    }

    public void addBoardListener(BoardListener bl){
        boardListeners.add(bl);
    }

    /**
    Kallar boardChanged för att uppdatera alla boardListeners
     */
    private void notifyListeners(){
        for (BoardListener bl : boardListeners) {
            bl.boardChanged();
        }
    }

    /**
    Roterar det fallande blocket
     */
    public void rotateFallingBlock(boolean rotateRight) {
        if(gameStatus == GameState.RUNNING && falling != null) {
            Poly oldPoly = falling;

            falling = oldPoly.rotatePoly(rotateRight);
            if (fallHandler.hasCollision(this, polyY)) {
                falling = oldPoly;
            }
            notifyListeners();
        }
    }

    /**
    Skapar ett nytt fallande block
     */
    private void newFallingBlock(){
        int x = rndSquare.nextInt(SquareType.values().length - 2);
        Poly falling = TetrominoMaker.getPoly(x);
        if (nextPowerup != null){
            fallHandler = nextPowerup;
            nextPowerup = null;
        } else fallHandler = new DefaultFallHandler();

        int xPos = BORDERSQUARES + width / 2 - falling.getSize() / 2;
        setFallingCords(xPos, BORDERSQUARES);
        this.falling = falling;
        if(fallHandler.hasCollision(this, polyY)){
            gameStatus = GameState.HIGHSCORE;
        }
    }

    /**
    Flyttar fallande blocket sidoleds
     */
    public void moveFallingBlockX(int moveX){
        if(gameStatus == GameState.RUNNING && falling != null) {
            polyX += moveX;
            if (fallHandler.hasCollision(this, polyY)) {
                polyX -= moveX;
            }
            notifyListeners();
        }
    }

    /**
    Kopierar det fallande blocket och sätter dessa blocktyper till en del av boardet
     */
    private void setBlockToBoard(Poly block){
        for (int i = 0; i < block.getSize(); i++) {
            for (int j = 0; j < block.getSize(); j++) {
                if(!block.getSquareType(j, i).equals(SquareType.EMPTY)){
                    squares[polyY + i][polyX + j] = block.getSquareType(j, i);
                }
            }
        }
    }

    /**
    Flyttar ner blocksen med hjälp av en lista med kordinater för de fulla raderna
     */
    private void moveDownBlocks(List<Integer> moveDownList){
        int yMove = 0;
        for (int i = moveDownList.get(moveDownList.size()-1); i > BORDERSQUARES; i--) {
            if(!moveDownList.isEmpty() && i == moveDownList.get(moveDownList.size() - 1)){
                moveDownList.remove(moveDownList.get(moveDownList.size()-1));
                yMove += 1;
            } else if (i + yMove > BORDERSQUARES) {
                for (int j = BORDERSQUARES; j < BORDERSQUARES + width; j++) {
                    squares[i + yMove][j] = getSquareType(j, i);
                    squares[i][j] = SquareType.EMPTY;
                }
            }
        }
    }

    /**
    Kollar om det finns rader som är fyllda och tar bort dem i sådanafall och flyttar ner blocksen över
     */
    private void checkCompleteRow(int y, int blockAmount){
        List<Integer> moveDownList = new ArrayList<>();
        for (int i = y; i < y+blockAmount; i++) {
            boolean completeRow = true;
            for (int j = BORDERSQUARES; j < width + BORDERSQUARES; j++) {
                if(squares[i][j].equals(SquareType.EMPTY) || squares[i][j].equals(SquareType.OUTSIDE)){
                    completeRow = false;
                    break;
                }
            }

            if (completeRow) {
                for (int j = BORDERSQUARES; j < width + BORDERSQUARES; j++) {
                    squares[i][j] = SquareType.EMPTY;
                }
                moveDownList.add(i);
            }
        }
        if(!moveDownList.isEmpty()) {
            addScore(moveDownList.size());
            moveDownBlocks(moveDownList);
            boolean heavyBlock = rndSquare.nextBoolean();
            if (heavyBlock){nextPowerup = new FallHeavy();}
            else {nextPowerup = new Fallthrough();}
        }
    }

    /**
    Flyttar fallande blocket nedåt
     */
    private void moveFallingBlockY(){
        polyY += 1;
        if(fallHandler.hasCollision(this, polyY - 1)){
            polyY -= 1;
            setBlockToBoard(falling);
            checkCompleteRow(polyY, falling.getSize());
            falling = null;
        }
    }

    /**
    Tick funktionen kallas varje gång timern "tickar", används för att föra spelet framåt
     */
    public void tick(){
        if (gameStatus == GameState.RUNNING) {
            if (falling == null) {
                newFallingBlock();
            } else {
                moveFallingBlockY();
            }
        }else if (gameStatus == GameState.HIGHSCORE){
            String input = JOptionPane.showInputDialog("Input name for high score list");
            highscore = new Highscore();
            highscore.setHighscore(input, score);
            gameStatus = GameState.FINISHED;
        }
        notifyListeners();
    }

    public static void main(String[] args) {
        Board newBoard = new Board(10, 10, true);
        System.out.println(BoardToTextConverter.convertToText(newBoard));
        System.out.println("--------");
        Board newBoard1 = new Board(5, 5, true);
        System.out.println(BoardToTextConverter.convertToText(newBoard1));
        System.out.println("--------");
        Board newBoard2 = new Board( 2, 2, true);
        System.out.println(BoardToTextConverter.convertToText(newBoard2));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Poly getFalling() {
        return falling;
    }

    public Highscore getHighscore() {
        return highscore;
    }

    public GameState getGameStatus() {
        return gameStatus;
    }

    public int getScore() {
        return score;
    }

    public void setFallingCords(int x, int y){
        polyX = x;
        polyY = y;
        notifyListeners();
    }

    public int getPolyX() {
        return polyX;
    }

    public int getPolyY() {
        return polyY;
    }

    public SquareType getSquareType(int x, int y){
        return squares[y][x];
    }

    public SquareType getSquareAt(int x, int y){
        if (falling == null){
            return squares[y][x];
        }

        int polyXPos = polyX;
        int polyYPos = polyY;
        int size = falling.getSize() - 1;

        if(polyXPos+size >= x && polyXPos <= x && polyYPos+size >= y && polyYPos <= y){
            int polyLocalX = x - polyXPos;
            int polyLocalY = y - polyYPos;

            if(falling.getSquareType(polyLocalX, polyLocalY) == SquareType.EMPTY){
                return squares[y][x];
            }
            else {
                return falling.getSquareType(polyLocalX, polyLocalY);
            }
        }
        else{
            return squares[y][x];
        }
    }

    public SquareType randomizeSquareType(){
        int x = rndSquare.nextInt(SquareType.values().length - 1);
        return SquareType.values()[x];
    }

    public void randomizeBoard(){
        squares = randomizeSquares(squares, width, height);
        notifyListeners();
    }

    /**
    Slumpar alla blocks i boardet
     */
    public SquareType[][] randomizeSquares(SquareType[][] squares, int width, int height){
        for (int i = 0; i < height + BORDERSQUARES * 2; i++) {
            for (int j = 0; j < width + BORDERSQUARES * 2; j++) {
                if (i >= BORDERSQUARES && i < height + BORDERSQUARES && j >= BORDERSQUARES && j < width + BORDERSQUARES){
                    squares[i][j] = randomizeSquareType();
                }
                else {
                    squares[i][j] = SquareType.OUTSIDE;
                }
            }
        }
        return squares;
    }
}
