package se.liu.ida.jonfa001.tddd78.tetris;

import java.util.ArrayList;
import java.util.List;

/**
 * Klassen för Heavy powerupen som har förmågan att trycka blocks neråt (om det finns plats)
 */
public class FallHeavy extends AbstractFallHandler
{
    @Override public boolean hasCollision(final Board gameBoard, int oldPolyY) {
	int polyX = gameBoard.getPolyX();
	int polyY = gameBoard.getPolyY();

        if (gameBoard.getFalling() == null){
	    return true;
	} else if (oldPolyY == polyY){
	    return super.hasCollision(gameBoard, oldPolyY);
	}

        boolean collision = false;
	List<Coordinates> collisionCoords = new ArrayList<>();
	for (int i = 0; i < gameBoard.getFalling().getSize(); i++) {
	    for (int j = 0; j < gameBoard.getFalling().getSize(); j++) {
		if (!gameBoard.getFalling().getSquareType(j, i).equals(SquareType.EMPTY) && squareTypeShouldCollide(gameBoard.getSquareType(j+polyX, i+polyY))){
		    collision = true;
		    if (gameBoard.getSquareType(j+polyX, i+polyY) != SquareType.OUTSIDE){
			collisionCoords.add(new Coordinates(j + polyX, i + polyY));
		    }
		}
	    }
	}
	if (!collision) return false; // Kolliderar inte med något
	if (collisionCoords.isEmpty()) return true; // Kolliderar med kanten
	return !gameBoard.attemptCollapseBlocks(collisionCoords); // Kolla om det går att trycka blocks neråt
    }

    @Override public boolean squareTypeShouldCollide(SquareType squareType) {
	return squareType != SquareType.EMPTY;
    }

    @Override public String getDescription() {
	return "Heavy";
    }
}
