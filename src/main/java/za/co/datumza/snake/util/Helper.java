package za.co.datumza.snake.util;

import za.co.datumza.snake.state.Square;

import java.util.Random;

public class Helper {
    private static final Random random = new Random();

    public static Square randomSquare(int boardWidth, int boardHeight) {
        int x = random.nextInt(boardWidth);
        int y = random.nextInt(boardHeight);

        return new Square(x, y);
    }
}
