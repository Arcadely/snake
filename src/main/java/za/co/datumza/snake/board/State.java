package za.co.datumza.snake.board;

import lombok.Getter;
import za.co.datumza.snake.player.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class State {
    private Board board;
    private List<Player> players;
    private Apple apple;

    public State(int boardWidth, int boardHeight, int playerCount) {
        this.board = new Board(boardWidth, boardHeight);
        this.apple = new Apple(board.getOpenSquare());

        createPlayers(playerCount);
    }

    public void progress() {
        for (Player player : players) {
            player.move(board, apple);
        }

        if (apple.isEaten()) {
            apple.move(board.getOpenSquare());
        }
    }

    private void createPlayers(int playerCount) {
        this.players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            Player player = new Player(i, board.getOpenSquare());
            this.players.add(player);
        }
    }
}
