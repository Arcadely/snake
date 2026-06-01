package za.co.datumza.snake.state;

import za.co.datumza.snake.player.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardState {
    private Board board;
    private List<Player> players;
    private Apple apple;

    public BoardState(int boardWidth, int boardHeight, int playerCount) {
        this.board = new Board(boardWidth, boardHeight);
        this.apple = new Apple(board.getOpenSquare());

        createPlayers(playerCount);
    }

    private void progress() {
        for (Player player : players) {
            player.move(board, apple);
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
