package za.co.datumza.snake.board;

import lombok.Getter;
import za.co.datumza.snake.player.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class State {
    private final int MAX_STATES = 5000;
    private final int STATE_INCREMENT = 10;

    private Board board;
    private List<Player> players;
    private List<Apple> apples;
    private int currentState;
    private boolean isGameOver;

    public State(int boardWidth, int boardHeight, int playerCount, int appleCount) {
        this.board = new Board(boardWidth, boardHeight);
        this.currentState = 0;
        this.isGameOver = false;

        createPlayers(playerCount);
        createApples(appleCount);
    }

    public void progress() {
        if (currentState >= MAX_STATES) {
            this.isGameOver = true;
            return;
        }

        for (Player player : players) {
            player.move(board, apples);
        }

        for (Apple apple : apples) {
            if (apple.isEaten()) {
                apple.move(board.getOpenSquare());
            }
        }

        currentState += STATE_INCREMENT;
    }

    private void createPlayers(int playerCount) {
        this.players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            Player player = new Player(i, board.getOpenSquare());
            this.players.add(player);
        }
    }

    private void createApples(int playerCount) {
        this.apples = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            Apple apple = new Apple(board.getOpenSquare());
            this.apples.add(apple);
        }
    }
}
