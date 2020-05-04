package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Klassen som har hand om fallande blockets rotation och dess struktur
 */
public class Poly {
    public SquareType[][] polyShape;

    public Poly(SquareType[][] shape) {
        this.polyShape = shape;
    }

    public SquareType getSquareType(int x, int y){
        return polyShape[y][x];
    }

    public int getSize() {
        return polyShape.length;
    }

    public Poly rotatePoly(boolean rotateRight){
        int size = getSize();
        Poly newPoly = new Poly(new SquareType[size][size]);
        for (int r = 0; r < getSize(); r++) {
            for (int c = 0; c < getSize(); c++) {
                if (rotateRight) {
                    newPoly.polyShape[c][getSize() - 1 - r] = polyShape[r][c];
                } else {
                    newPoly.polyShape[getSize() - 1 - c][r] = polyShape[r][c];
                }
            }
        }
        return newPoly;
    }
}
