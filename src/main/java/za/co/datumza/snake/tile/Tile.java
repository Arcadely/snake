package za.co.datumza.snake.tile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class Tile {
    @Getter
    protected static final int tileSize = 10;

    protected int x;
    protected int y;

    public Tile(Tile tile) {
        this.x = tile.x;
        this.y = tile.y;
    }

    public int convert(int value) {
        return value * tileSize;
    }

    public int convertX() {
        return convert(x);
    }

    public int convertY() {
        return convert(y);
    }

    public void draw(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(convert(x), convert(y), tileSize, tileSize);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
