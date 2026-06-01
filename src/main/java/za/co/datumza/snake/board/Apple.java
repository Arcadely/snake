package za.co.datumza.snake.board;

import lombok.Getter;
import za.co.datumza.snake.player.Player;

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

    public void eat(Player player) {
        this.isEaten = true;
        position.block(player.getId());
    }

    private void resetTimer() {
        this.timer = TIMER;
    }
}
