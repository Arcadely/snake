package za.co.datumza.snake.game;

import lombok.AllArgsConstructor;
import za.co.datumza.snake.board.Square;

import java.awt.*;

@AllArgsConstructor
public class Visualiser {
    private int tileSize;

    public void draw(Graphics g, Color color, Square square) {
        g.setColor(color);
        g.fillRect(convert(square.getX()), convert(square.getY()), tileSize, tileSize);

        System.out.println(square.getX() + ", " + square.getY());
    }

    public int convert(int value) {
        return value * tileSize;
    }
}
