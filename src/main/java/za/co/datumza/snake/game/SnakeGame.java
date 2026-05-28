package za.co.datumza.snake.game;

import za.co.datumza.snake.tile.Food;
import za.co.datumza.snake.tile.Snake;
import za.co.datumza.snake.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int REFRESH = 100;
    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize = Tile.getTileSize();
    private final Random random = new Random();
    private final Timer gameLoop =  new Timer(REFRESH, this);

    Snake snakeHead;
    Food food;

    public SnakeGame(int boardWidth, int boadrHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boadrHeight;

        setupBoard();

        // Players + Food
        snakeHead = new Snake(5, 5);
        food = new Food(boardWidth, boardHeight, random);

        // Loop game every N ms
        gameLoop.start();
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

    private void move() {
        snakeHead.move();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }
}
