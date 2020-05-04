package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Test klass för tetris
 */
public class BoardTest {
    public static void main(String[] args) {
        Board newBoard = new Board(8, 15, false);
        TetrisViewer window = new TetrisViewer(newBoard);
        window.show();
    }
}
