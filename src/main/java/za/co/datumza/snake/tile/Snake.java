package za.co.datumza.snake.tile;

public class Snake extends Tile {
    private int velocityX = 1;
    private int velocityY = 0;

    public Snake(int x, int y) {
        super(x, y);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        this.x += velocityX;
        this.y += velocityY;
    }
}
