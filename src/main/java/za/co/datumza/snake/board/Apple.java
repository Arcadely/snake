package za.co.datumza.snake.board;

import lombok.Getter;

@Getter
public class Apple {
    private final int TIMER = 500;
    private Square position;
    private boolean isEaten;

    @Getter
    private int timer;

    public Apple(Square square) {
        move(square);
    }

    public void move(Square square) {
        this.position = square;
        this.isEaten = false;
        resetTimer();
    }

    public void eat() {
        this.isEaten = true;
    }

    private void resetTimer() {
        this.timer = TIMER;
    }
}
