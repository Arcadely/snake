package za.co.datumza.snake.tile;

import java.util.Random;

public class Food extends Tile {
    public Food(int x, int y) {
        super(x, y);
    }

    public Food(int boardWidth, int boardHeight, Random random) {
        int x = random.nextInt(boardWidth/tileSize);
        int y = random.nextInt(boardHeight/tileSize);
        super(x, y);
    }
}
