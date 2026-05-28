package za.co.datumza.snake.game;

import za.co.datumza.snake.tile.Food;
import za.co.datumza.snake.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SnakeGame extends JPanel {
    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize = Tile.getTileSize();
    private final Random random = new Random();

    Tile snakeHead;
    Tile food;

    public SnakeGame(int boardWidth, int boadrHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boadrHeight;

        setupBoard();

        snakeHead = new Tile(5, 5);
        food = new Food(boardWidth, boardHeight, random);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // drawGrid(g);
        drawSnake(g);
        drawFood(g);
    }

    private void setupBoard() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
    }

    private void drawSnake(Graphics g) {
        snakeHead.draw(g, Color.green);
    }

    private void drawFood(Graphics g) {
        food.draw(g, Color.red);
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
    }
}
