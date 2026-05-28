package za.co.datumza.snake.game;

import javax.swing.*;
import java.awt.*;

public class SnakeGame extends JPanel {
    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize = Tile.getTileSize();

    Tile snakeHead;

    public SnakeGame(int boardWidth, int boadrHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boadrHeight;

        setupBoard();

        snakeHead = new Tile(5, 5);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        draw(g);

    }

    private void setupBoard() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
    }

    private void draw(Graphics g) {
        snakeHead.draw(g, Color.green);
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
    }
}
