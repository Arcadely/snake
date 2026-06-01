package za.co.datumza.snake.player;

import lombok.Getter;
import za.co.datumza.snake.board.Apple;
import za.co.datumza.snake.board.Board;
import za.co.datumza.snake.board.Square;

import java.util.List;

@Getter
public class Player {
    private int id;

    private Movement movement;
    private Square position;
    private boolean isAlive = true;
    private Stats stats;

    public Player(int id, Square position) {
        position.block(id);

        this.id = id;
        this.position = position;
        this.movement = new Movement(Direction.random());
        this.stats = new Stats();
    }

    public void changeDirection(Direction direction) {
        this.movement.setDirection(direction);
    }

    public void move(Board board, List<Apple> apples) {
        try {
            if (!isAlive) {
                this.position = board.getOpenSquare();
                this.isAlive = true;
            }

            this.position = getNextSquare(board, movement.getDirection());
            eat(apples);
            checkCollision(board);

        } catch (Exception e) {
            die(board);
        }
    }

    public void eat(List<Apple> apples) {
        for (Apple apple : apples) {
            if (position.equals(apple.getPosition())) {
                this.stats.eat();
                apple.eat();
            }
        }
    }

    public void die(Board board) {
        this.isAlive = false;
        this.stats.die();
        this.position = board.getOpenSquare();
    }

    public void kill() {
        this.stats.kill();
    }

    public void checkCollision(Board board) {
        try {
            Square square = board.getSquare(position);

            if (square.isBlocked(this.id)) {
                square.unblock(this.id);
                die(board);
            }
        } catch (Exception e) {
            die(board);
        }
    }

    private Square getNextSquare(Board board, Direction direction) {
        return switch (direction) {
            case UP -> position.getUp(board);
            case DOWN -> position.getDown(board);
            case LEFT -> position.getLeft(board);
            case RIGHT -> position.getRight(board);
        };
    }
}
