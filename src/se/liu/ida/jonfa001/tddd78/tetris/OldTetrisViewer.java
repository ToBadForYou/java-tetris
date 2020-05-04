package se.liu.ida.jonfa001.tddd78.tetris;

import javax.swing.*;
import java.awt.*;

/**
 * Gamla tetrisviewer klassen, användes för att visa en textruta med boarden representerad som text
 */
public class OldTetrisViewer
{
    private Board gameBoard;
    private JTextArea textArea;

    public OldTetrisViewer(Board gameBoard) {
        this.gameBoard = gameBoard;
        textArea = new JTextArea(gameBoard.getWidth(), gameBoard.getHeight());
    }

    public void updateBoard(){
        textArea.setText(BoardToTextConverter.convertToText(gameBoard));
    }

    public void show(){
        JFrame frame = new JFrame();
        String boardText = BoardToTextConverter.convertToText(gameBoard);
        textArea.setText(boardText);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        frame.setLayout(new BorderLayout());
        frame.add(textArea, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
