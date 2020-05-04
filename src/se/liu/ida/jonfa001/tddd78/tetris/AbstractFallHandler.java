package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Abstrakta klassen för FallHandler
 */
public abstract class AbstractFallHandler implements FallHandler
{
    @Override public boolean squareTypeShouldCollide(SquareType squareType) {
	return false;
    }

    @Override public boolean hasCollision(final Board gameBoard, int polyY) {
	if (gameBoard.getFalling() == null){
	    return true;
	}
	for (int i = 0; i < gameBoard.getFalling().getSize(); i++) {
	    for (int j = 0; j < gameBoard.getFalling().getSize(); j++) {
	        if (!gameBoard.getFalling().getSquareType(j, i).equals(SquareType.EMPTY) && squareTypeShouldCollide(gameBoard.getSquareType(j+gameBoard.getPolyX(), i+gameBoard.getPolyY()))){
	            return true;
		}
	    }
	}
	return false;
    }

    @Override public String getDescription() {
	return "None";
    }
}