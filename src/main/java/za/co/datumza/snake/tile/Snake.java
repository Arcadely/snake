package za.co.datumza.snake.tile;

import lombok.Setter;

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

    public void setVelocityX(int velocityX) {
        if (this.velocityX != 0) {
            return;
        }

        this.velocityX = velocityX;
        this.velocityY = 0;
    }

    public void setVelocityY(int velocityY) {
        if (this.velocityY != 0) {
            return;
        }

        this.velocityY = velocityY;
        this.velocityX = 0;
    }
}
