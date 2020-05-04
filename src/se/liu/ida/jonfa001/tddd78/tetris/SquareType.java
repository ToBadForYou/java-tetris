package se.liu.ida.jonfa001.tddd78.tetris;

import java.util.Random;

/**
 * Enum för de olika blocktyperna
 */
public enum SquareType {
    I, O, T, S, Z, J, L, EMPTY, OUTSIDE;

    public static void main(String[] args){
        Random rnd = new Random();
        for (int i = 0; i < 25; i++) {
            int rndIndex = rnd.nextInt(SquareType.values().length);
            System.out.println(SquareType.values()[rndIndex]);
        }
    }
}
