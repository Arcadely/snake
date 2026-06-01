package za.co.datumza.snake.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movement {
    private Direction direction;
    private int velocityX;
    private int velocityY;

    public Movement(Direction direction) {
        this.direction = direction;
        move(direction);
    }

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                up();
                break;

            case DOWN:
                down();
                break;

            case LEFT:
                left();
                break;

            case RIGHT:
                right();
                break;
        }
    }

    public void move(int direction) {
        move(Direction.fromValue(direction));
    }

    public void up() {
        this.velocityX = 0;
        this.velocityY = 1;
    }

    public void down() {
        this.velocityX = 0;
        this.velocityY = -1;
    }

    public void left() {
        this.velocityX = -1;
        this.velocityY = 0;
    }

    public void right() {
        this.velocityX = 1;
        this.velocityY = 0;
    }
}
