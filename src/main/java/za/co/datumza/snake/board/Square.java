package za.co.datumza.snake.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Square {
    private final int x;
    private final int y;
    private Set<Integer> players;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;

        this.players = new HashSet<>();
    }

    public void block(int player) {
        this.players.add(player);
    }

    public void unblock(int player) {
        this.players.remove(player);
    }

    public boolean isOpen() {
        return this.players.isEmpty();
    }

    public boolean isBlocked() {
        return !this.players.isEmpty();
    }

    public Square getUp(Board board) {
        return board.getSquare(this.x , this.y - 1);
    }

    public Square getDown(Board board) {
        return board.getSquare(this.x, this.y + 1);
    }

    public Square getLeft(Board board) {
        return board.getSquare(this.x - 1, this.y);
    }

    public Square getRight(Board board) {
        return board.getSquare(this.x + 1, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return this.x == square.x && this.y == square.y;
    }
}
