package za.co.datumza.snake.tile;

import java.util.Random;

public class Food extends Tile {
    private final int boardWidth;
    private final int boardHeight;
    private final Random random;

    public Food(int boardWidth, int boardHeight, Random random) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.random = random;


        super(0, 0);
        move();
    }

    public void move() {
        x = random.nextInt(boardWidth/tileSize);
        y = random.nextInt(boardHeight/tileSize);
    }
}
