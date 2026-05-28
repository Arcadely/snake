package za.co.datumza.snake.game;

import javax.swing.*;
import java.awt.*;

public class SnakeGame extends JPanel {
    private final int boardWidth;
    private final int boadrHeight;

    Tile snakeHead;

    public SnakeGame(int boardWidth, int boadrHeight) {
        this.boardWidth = boardWidth;
        this.boadrHeight = boadrHeight;

        setupBoard();

        snakeHead = new Tile(5, 5);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void setupBoard() {
        setPreferredSize(new Dimension(boardWidth, boadrHeight));
        setBackground(Color.black);
    }

    private void draw(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(snakeHead.getX(), snakeHead.getY(), Tile.getTileSize(), Tile.getTileSize());
    }
}
