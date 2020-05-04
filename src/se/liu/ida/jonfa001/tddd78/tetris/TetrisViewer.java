package se.liu.ida.jonfa001.tddd78.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klassen för tetris spelets själva huvudruta
 */
public class TetrisViewer{
    private TetrisComponent gameWindow;

    public TetrisViewer(Board gameBoard) {
        TetrisComponent gameWindow = new TetrisComponent(gameBoard);
        this.gameWindow = gameWindow;
        gameWindow.setScoreDisplay(new ScoreDisplay(gameWindow));
    }

    private static void menuExit(ActionEvent evt, JFrame frame) {
        Object[] options = {"Ja",
                "Nej!"};
        int n = JOptionPane.showOptionDialog(frame,
                                             "Är du säker?",
                                             "Avsluta",
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null,
                                             options,
                                             options[0]);
        if(n == JOptionPane.OK_OPTION)
        {
            System.exit(0);
        }
    }

    public void show(){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(gameWindow, BorderLayout.CENTER);
        frame.add(gameWindow.getScoreDisplay(), BorderLayout.EAST);
        //frame.add(new IconPainter(), BorderLayout.WEST);

        final JMenuBar menuBar = new JMenuBar();

        final JMenu menu = new JMenu("Menu");
        JMenuItem avsluta = new JMenuItem("Avsluta");
        menu.add(avsluta);
        menuBar.add(menu);

        avsluta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                menuExit(evt, frame);
            }
        });

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
}
