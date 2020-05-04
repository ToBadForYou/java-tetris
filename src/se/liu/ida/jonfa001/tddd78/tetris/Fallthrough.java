package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Klassen för blocket "Fallthrough" som endast kolliderar med kanterna av spelplanen
 */
public class Fallthrough extends AbstractFallHandler
{
    @Override public boolean squareTypeShouldCollide(SquareType squareType) {
	return squareType == SquareType.OUTSIDE;
    }

    @Override public String getDescription() {
	return "Through";
    }
}
