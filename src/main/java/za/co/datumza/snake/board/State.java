package za.co.datumza.snake.board;

import lombok.Getter;
import za.co.datumza.snake.player.Direction;
import za.co.datumza.snake.player.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class State {
    private final int MAX_STATES = 5000;
    private final int STATE_INCREMENT = 1;

    private Board board;
    private List<Player> players;
    private List<Apple> apples;
    private List<Square> cleanupSquares;
    private int currentState;
    private boolean isGameOver;

    public State(int boardWidth, int boardHeight, int playerCount, int appleCount) {
        this.board = new Board(boardWidth, boardHeight);
        this.cleanupSquares = new ArrayList<>();
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

        List<Square> nextSquares = getNextSquares();
        checkBlockedSquareCollisions(nextSquares);
        checkHeadOnCollisions(nextSquares);

        for (Player player : players) {
            if (!player.isAlive()) {
                cleanupSquares.addAll(player.getBody());
            }
        }

        for (Player player : players) {
            player.handleMove(board, apples);
        }

        for (Square square : cleanupSquares) {
            square.getPlayers().clear();
        }

        cleanupSquares.clear();

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

    public void onKeyPress(Direction direction) {
        this.players.getFirst().getMovement().setDirection(direction);
    }

    private List<Square> getNextSquares() {
        List<Square> nextSquares = new ArrayList<>();

        for (Player player : players) {
            try {
                nextSquares.add(player.getNextSquare(board));
            } catch (Exception e) {
                player.die(board);
                nextSquares.add(null);
            }
        }

        return nextSquares;
    }

    private void checkBlockedSquareCollisions(List<Square> nextSquares) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Square nextSquare = nextSquares.get(i);

            if (player.isAlive() && nextSquare != null && nextSquare.isBlocked()) {
                player.die(board);
                awardKills(player, nextSquare);
            }
        }
    }

    private void checkHeadOnCollisions(List<Square> nextSquares) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Square nextSquare = nextSquares.get(i);

            if (!player.isAlive() || nextSquare == null) {
                continue;
            }

            List<Player> collidingPlayers = new ArrayList<>();
            collidingPlayers.add(player);

            for (int j = i + 1; j < players.size(); j++) {
                Player otherPlayer = players.get(j);
                Square otherNextSquare = nextSquares.get(j);

                if (otherPlayer.isAlive() && nextSquare.equals(otherNextSquare)) {
                    collidingPlayers.add(otherPlayer);
                }
            }

            if (collidingPlayers.size() > 1) {
                awardHeadOnKills(collidingPlayers);
            }
        }
    }

    private void awardHeadOnKills(List<Player> collidingPlayers) {
        for (Player victim : collidingPlayers) {
            victim.die(board);
        }

        for (Player killer : collidingPlayers) {
            for (Player victim : collidingPlayers) {
                if (killer.getId() != victim.getId()) {
                    killer.kill();
                }
            }
        }
    }

    private void awardKills(Player victim, Square collisionSquare) {
        for (Integer playerId : collisionSquare.getPlayers()) {
            if (playerId != victim.getId()) {
                players.get(playerId).kill();
            }
        }
    }
}
