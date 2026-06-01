package za.co.datumza.snake.game;

import za.co.datumza.snake.board.Apple;
import za.co.datumza.snake.board.State;
import za.co.datumza.snake.player.Player;
import za.co.datumza.snake.tile.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    protected static final int tileSize = 10;
    private final int REFRESH = 100;

    private final int SCORE_MULTIPLIER = 5;
    private final boolean isEndless = true;
    private final int boardWidth;
    private final int boardHeight;
    private final Timer gameLoop = new Timer(REFRESH, this);

    private int currentState = 0;
    private boolean isPaused = false;

    private State state;
    private Visualiser visualiser;

    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        this.state = new State(boardWidth/tileSize, boardHeight/tileSize, 4, 200); // todo
        this.visualiser = new Visualiser(tileSize); // todo

        setupBoard();

        // Loop game every N ms
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//         drawGrid(g);
        drawApples(g);
        drawPlayers(g);

        showData(g);
    }

    private void setupBoard() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
    }

    private void drawPlayers(Graphics g) {
        for (Player player : state.getPlayers()) {
            if (player.isAlive()) {
                visualiser.draw(g, Color.red, player.getPosition());
            }
        }
    }

    private void drawApples(Graphics g) {
        for (Apple apple : state.getApples()) {
            visualiser.draw(g, Color.green, apple.getPosition());
        }
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
    }

    private void move() {
        if (isPaused) {
            return;
        }

        state.progress();
    }

    private void showData(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        visualiser.drawState(g, state);

        for (Player player : state.getPlayers()) {
            visualiser.drawPlayerStats(g, Color.red, player);
        }
    }



    private void showPlayerScore(Graphics g, Snake snake, int index) {
        String info = isEndless ? String.format("%s: %d", snake.getName(), snake.getMaxPoints() * SCORE_MULTIPLIER) : String.format("%s (%d Lives): %d", snake.getName(), snake.getLives(), snake.getMaxPoints() * SCORE_MULTIPLIER);

        g.setColor(snake.getColor());
        g.drawString(info, tileSize, tileSize + 25 + (22 * index));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state.isGameOver() && !isEndless) {
            gameLoop.stop();
            return;
        }

        move();
        repaint();
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

//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_LEFT, KeyEvent.VK_A:
//                snake.setVelocityX(-1);
//                break;
//
//            case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
//                snake.setVelocityX(1);
//                break;
//
//            case KeyEvent.VK_UP, KeyEvent.VK_W:
//                snake.setVelocityY(-1);
//                break;
//
//            case KeyEvent.VK_DOWN, KeyEvent.VK_S:
//                snake.setVelocityY(1);
//                break;
//
//            default:
//                break;
//    }
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
