package za.co.datumza.snake.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class Tile {
    @Getter
    private static final int tileSize = 10;

    private int x;
    private int y;

    public int convert(int value) {
        return value * tileSize;
    }

    public void draw(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(convert(x), convert(y), tileSize, tileSize);
    }
}
