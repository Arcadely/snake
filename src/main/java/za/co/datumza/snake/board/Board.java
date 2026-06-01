package za.co.datumza.snake.board;

import za.co.datumza.snake.player.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    private final int boardWidth;
    private final int boardHeight;

    ArrayList<ArrayList<Square>> rows = new ArrayList<>();

    public Board(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        inititalise();
    }

    public Square getSquare(int x, int y) {
        ArrayList<Square> row = rows.get(y);
        return row.get(x);
    }

    public Square getSquare(Square square) {
        return getSquare(square.getX(), square.getY());
    }

    public void block(Square square, int player) {
        getSquare(square).block(player);
    }

    public void unblock(Square square, int player) {
        getSquare(square).unblock(player);
    }

//    public boolean isOpen(int x, int y) {
//        return rows.stream()
//                .flatMap(List::stream)
//                .anyMatch(square ->
//                        !square.isBlocked()
//                                && square.getX() == x
//                                && square.getY() == y);
//    }

    public Square getOpenSquare() {
        List<Square> availableSquares = getOpenSquares();
        return availableSquares.get(
                ThreadLocalRandom.current().nextInt(availableSquares.size())
        );
    }

    public Square getOpenSnakeHead(int snakeLength, Direction direction) {
        List<Square> availableSquares = getOpenSquares().stream()
                .filter(square -> canPlaceSnake(square, snakeLength, direction))
                .toList();

        if (availableSquares.isEmpty()) {
            return getOpenSquare();
        }

        return availableSquares.get(
                ThreadLocalRandom.current().nextInt(availableSquares.size())
        );
    }

    public Square getSnakeBodySquare(Square head, Direction direction, int offset) {
        return switch (direction) {
            case UP -> getSquare(head.getX(), head.getY() + offset);
            case DOWN -> getSquare(head.getX(), head.getY() - offset);
            case LEFT -> getSquare(head.getX() + offset, head.getY());
            case RIGHT -> getSquare(head.getX() - offset, head.getY());
        };
    }

    private void inititalise () {
        for (int y = 0; y < boardHeight; y++) {
            ArrayList<Square> row = new ArrayList<>();

            for (int x = 0; x < boardWidth; x++) {
                Square block = new Square(x, y);
                row.add(block);
            }

            rows.add(row);
        }
    }

    public List<Square> getOpenSquares() {
        return rows.stream()
                .flatMap(List::stream)
                .filter(Square::isOpen)
                .toList();
    }

    public List<Square> getBlockedSquares() {
        return rows.stream()
                .flatMap(List::stream)
                .filter(square -> !square.isOpen())
                .toList();
    }

    private boolean canPlaceSnake(Square head, int snakeLength, Direction direction) {
        for (int i = 0; i < snakeLength; i++) {
            try {
                if (!getSnakeBodySquare(head, direction, i).isOpen()) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }
}
