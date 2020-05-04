package se.liu.ida.jonfa001.tddd78.tetris;

/**
 * Klassen som generarar Poly strukturerna för de olika block typerna
 */
public class TetrominoMaker {
    public int getNumberOfTypes(){
        return SquareType.values().length;
    }

    public static Poly generateIBlock(){
        SquareType[][] blockInfo = {{SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY},
                {SquareType.I, SquareType.I, SquareType.I, SquareType.I},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}};
        return new Poly(blockInfo);
    }

    public static Poly generateJBlock(){
        SquareType[][] blockInfo = {{SquareType.J, SquareType.EMPTY, SquareType.EMPTY},
                {SquareType.J, SquareType.J, SquareType.J,},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}};
        return new Poly(blockInfo);
    }

    public static Poly generateLBlock(){
        SquareType[][] blockInfo = {{SquareType.EMPTY, SquareType.EMPTY, SquareType.L},
                {SquareType.L, SquareType.L, SquareType.L,},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}};
        return new Poly(blockInfo);
    }

    public static Poly generateOBlock(){
        SquareType[][] blockInfo = {{SquareType.O, SquareType.O}, {SquareType.O, SquareType.O}};
        return new Poly(blockInfo);
    }

    public static Poly generateSBlock() {
        SquareType[][] blockInfo = {{SquareType.EMPTY, SquareType.S, SquareType.S},
                {SquareType.S, SquareType.S, SquareType.EMPTY,},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}};
        return new Poly(blockInfo);
    }

    public static Poly generateTBlock() {
        SquareType[][] blockInfo = {{SquareType.EMPTY, SquareType.T, SquareType.EMPTY},
                {SquareType.T, SquareType.T, SquareType.T,},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}};
        return new Poly(blockInfo);
    }

    public static Poly generateZBlock() {
        SquareType[][] blockInfo = {{SquareType.Z, SquareType.Z, SquareType.EMPTY},
                {SquareType.EMPTY, SquareType.Z, SquareType.Z,},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}};
        return new Poly(blockInfo);
    }

    public static Poly getPoly(int n){
        switch(n){
            case 0:
                return generateIBlock();
            case 1:
                return generateJBlock();
            case 2:
                return generateLBlock();
            case 3:
                return generateOBlock();
            case 4:
                return generateSBlock();
            case 5:
                return generateTBlock();
            case 6:
                return generateZBlock();
            default:
                throw new IllegalArgumentException("Invalid index: " + n);
        }
    }
}
