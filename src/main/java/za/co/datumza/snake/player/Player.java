package za.co.datumza.snake.player;

import lombok.Getter;
import za.co.datumza.snake.board.Apple;
import za.co.datumza.snake.board.Board;
import za.co.datumza.snake.board.Square;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Player {
    public static final int DEFAULT_SIZE = 6;

    private int id;
    private PlayerType type;
    private Movement movement;
    private List<Square> body;
    private boolean isAlive = true;
    private Stats stats;

    public Player(int id, PlayerType type, Direction direction, Square position, Board board) {
        this.id = id;
        this.type = type;
        this.movement = new Movement(direction);
        this.stats = new Stats();

        initialise(position, board);
    }

    public boolean isZombie() {
        return type == PlayerType.ZOMBIE;
    }

    public boolean isPlayer() {
        return type == PlayerType.PLAYER;
    }

    public void changeDirection(Direction direction) {
        this.movement.setDirection(direction);
    }

    public void handleMove(Board board, List<Apple> apples) {
        try {
            if (!isAlive) {
                respawn(board);
            }

            if (isPlayer()) {
                eat(apples);
            }

            moveBody(board);

        } catch (Exception e) {
            die(board);
        }
    }

    public void moveBody(Board board) {
        Square nextHead = getNextSquare(board, movement.getDirection());
        nextHead.block(id);
        this.body.addFirst(nextHead);

        // Unblocks both tail and next tail since they are the same squares
        getTail().unblock(this.id);
        this.body.removeLast();

        // Do this because tail and next tail have both been unblocked
        getTail().block(this.id);
    }

    public void eat(List<Apple> apples) {
        for (Apple apple : apples) {
            Square applePosition = apple.getPosition();

            if (getHead().equals(applePosition)) {
                apple.eat(this);
                this.body.add(getTail());
                this.stats.eat();
            }
        }
    }

    public void die(Board board) {
        if (isZombie()) {
            return;
        }

        if (!this.isAlive) {
            return;
        }

        this.isAlive = false;
        this.stats.die();
    }

    public void kill() {
        this.stats.kill();
    }

    public Square checkCollision(Board board) {
        try {
            Square nextSquare = getNextSquare(board);

            if (nextSquare.isBlocked()) {
                die(board);
                return nextSquare;
            }
        } catch (Exception e) {
            die(board);
        }

        return null;
    }

    public Square getNextSquare(Board board) {
        return getNextSquare(board, movement.getDirection());
    }

    public Square getNextSquare(Board board, Direction direction) {
        return switch (direction) {
            case UP -> getHead().getUp(board);
            case DOWN -> getHead().getDown(board);
            case LEFT -> getHead().getLeft(board);
            case RIGHT -> getHead().getRight(board);
        };
    }

    public Square getHead() {
        return this.body.getFirst();
    }

    public Square getTail() {
        return this.body.getLast();
    }

    private void respawn(Board board) {
        Direction direction = Direction.random();
        this.movement = new Movement(direction);
        initialise(board.getOpenSnakeHead(DEFAULT_SIZE, direction), board);
    }

    private void initialise(Square head, Board board) {
        this.body = new LinkedList<>();

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            Square bodyPart = board.getSnakeBodySquare(head, movement.getDirection(), i);
            bodyPart.block(this.id);
            this.body.add(bodyPart);
        }

        this.isAlive = true;
    }
}
