package za.co.datumza.snake.game;

import za.co.datumza.snake.tile.Food;
import za.co.datumza.snake.tile.Snake;
import za.co.datumza.snake.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int REFRESH = 100;
    private final int MAX_STATES = 5000;
    private final int STATE_INCREMENT = 10;
    private final int SCORE_MULTIPLIER = 5;
    private final boolean isEndless = true;
    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize = Tile.getTileSize();
    private final Random random = new Random();
    private final Timer gameLoop =  new Timer(REFRESH, this);

    private int currentState = 0;
    private boolean isPaused = false;

    private List<Snake> snakes;
    private Snake snake;
    private Food food;

    public SnakeGame(int boardWidth, int boadrHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boadrHeight;

        setupBoard();

        snake = new Snake("Player", Color.pink, boardWidth, boardHeight, isEndless, random);

        // Players + Food
        snakes = List.of(
                new Snake("1", Color.green, boardWidth, boardHeight, isEndless, random),
                new Snake("2", Color.yellow, boardWidth, boardHeight, isEndless, random),
                new Snake("3", Color.red, boardWidth, boardHeight, isEndless, random),
                new Snake("4", Color.blue, boardWidth, boardHeight, isEndless, random),
                snake
        );

        food = new Food(boardWidth, boardHeight, random);

        // Loop game every N ms
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // drawGrid(g);
        drawFood(g);
        drawSnake(g);

        showData(g);
    }

    private void setupBoard() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
    }

    private void drawSnake(Graphics g) {
        for (Snake snake : snakes) {
            snake.draw(g);
        }
    }

    private void drawFood(Graphics g) {
        food.draw(g, Color.green);
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
    }

    private void move() {
        boolean hasEaten = false;

        if (isPaused) {
            return;
        }

        for (Snake snake : snakes) {
            hasEaten = snake.move(food) || hasEaten;
        }

        if (hasEaten) {
            food.move();
        }

        currentState += STATE_INCREMENT;
    }

    private void showData(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        showGameState(g);

        for (int i = 0; i < snakes.size(); i++) {
            showPlayerScore(g, snakes.get(i), i + 1);
        }
    }

    private void showGameState(Graphics g) {
        String info = isEndless ? String.format("State: %d", currentState) : String.format("State: %d/%d", currentState, MAX_STATES);

        g.setColor(Color.white);
        g.drawString(info, tileSize, tileSize + 16);
    }

    private void showPlayerScore(Graphics g, Snake snake, int index) {
        String info = isEndless ? String.format("%s: %d", snake.getName(), snake.getMaxPoints() * SCORE_MULTIPLIER) : String.format("%s (%d Lives): %d", snake.getName(), snake.getLives(), snake.getMaxPoints() * SCORE_MULTIPLIER);

        g.setColor(snake.getColor());
        g.drawString(info, tileSize, tileSize + 25 + (22 * index));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((isGameOver() || (currentState >= MAX_STATES)) && !isEndless) {
            gameLoop.stop();
            return;
        }

        move();
        repaint();
    }

    private boolean isGameOver() {
        return snakes.stream().anyMatch(s -> !s.getIsAlive());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            isPaused = !isPaused;
            return;
        }

        if (isPaused) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                snake.setVelocityX(-1);
                break;

            case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                snake.setVelocityX(1);
                break;

            case KeyEvent.VK_UP, KeyEvent.VK_W:
                snake.setVelocityY(-1);
                break;

            case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                snake.setVelocityY(1);
                break;

            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //dont need
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // dont need
    }
}
