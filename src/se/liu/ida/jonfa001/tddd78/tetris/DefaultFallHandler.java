package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Standard klassen för Falling, fungerar som ett vanligt tetrisblock
 */
public class DefaultFallHandler extends AbstractFallHandler
{
    @Override public boolean squareTypeShouldCollide(SquareType squareType) {
	return squareType != SquareType.EMPTY;
    }
}
