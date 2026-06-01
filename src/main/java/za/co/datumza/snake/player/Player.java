package za.co.datumza.snake.player;

import lombok.Getter;
import za.co.datumza.snake.state.Apple;
import za.co.datumza.snake.state.Board;
import za.co.datumza.snake.state.Square;

public class Player {
    private int id;

    private Movement movement;
    private Square position;
    private boolean isAlive = true;

    @Getter
    private Stats stats;

    public Player(int id, Square position) {
        position.block(id);

        this.id = id;
        this.position = position;
    }

    public void changeDirection(Direction direction) {
        this.movement.setDirection(direction);
    }

    public void move(Board board, Apple apple) {
        if (!isAlive) {
            this.position = board.getOpenSquare();
            this.isAlive = true;
        }

        this.position = getNextSquare(board, movement.getDirection());
        checkCollision(board, apple);
    }

    public void eat(Apple apple) {
        if (position.equals(apple.getPosition())) {
            this.stats.eat();
            apple.eat();
        }
    }

    public void die() {
        this.isAlive = false;
        this.stats.die();
    }

    public void kill() {
        this.stats.kill();
    }

    public void checkCollision(Board board, Apple apple) {
        eat(apple);

        Square square = board.getSquare(position);

        if (square.isBlocked(this.id)) {
            square.unblock(this.id);
            kill();
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
