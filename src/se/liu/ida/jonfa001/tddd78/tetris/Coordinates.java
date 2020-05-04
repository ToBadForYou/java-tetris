package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Klass för kordinater, används för Heavy klassen främst för att hålla reda på tomma blocks positioner
 */
public class Coordinates
{
    private int x,y, emptyY;

    public Coordinates(final int x, final int y) {
	this.x = x;
	this.y = y;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public void setEmptyY(final int emptyY) {
	this.emptyY = emptyY;
    }

    public int getEmptyY() {
	return emptyY;
    }
}
