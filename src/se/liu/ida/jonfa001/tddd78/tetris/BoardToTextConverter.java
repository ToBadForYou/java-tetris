package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Klass som konverterar ett board till en rad med strängar som representerar planens block
 */
public class BoardToTextConverter {
    public static String convertToText(Board board) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                builder.append(findSquareTypeID(board.getSquareAt(i, j)));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String findSquareTypeID(SquareType squareType) {
        switch (squareType) {
            case I:
                return "i";
            case J:
                return "j";
            case L:
                return "l";
            case O:
                return "o";
            case S:
                return "s";
            case T:
                return "t";
            case Z:
                return "z";
            case EMPTY:
            default:
                return " ";
        }
    }
}
