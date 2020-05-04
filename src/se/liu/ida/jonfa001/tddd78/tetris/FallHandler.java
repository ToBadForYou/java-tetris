package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Interface för FallHandler
 */
public interface FallHandler
{
    public boolean hasCollision(Board gameBoard, int polyY);
    public boolean squareTypeShouldCollide(SquareType squareType);
    public String getDescription();
}
