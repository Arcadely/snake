package za.co.datumza.snake.game;

import za.co.datumza.snake.board.Apple;
import za.co.datumza.snake.board.Square;
import za.co.datumza.snake.board.State;
import za.co.datumza.snake.player.Direction;
import za.co.datumza.snake.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    protected static final int tileSize = 10;
    private final int REFRESH = 100;
    private static final int SCOREBOARD_WIDTH = 220;
    private static final Color[] PLAYER_COLORS = {
            new Color(239, 68, 68),
            new Color(59, 130, 246),
            new Color(34, 197, 94),
            new Color(245, 158, 11),
            new Color(168, 85, 247),
            new Color(20, 184, 166)
    };

    private final boolean isEndless = true;
    private final int boardWidth;
    private final int boardHeight;
    private final Timer gameLoop = new Timer(REFRESH, this);

    private boolean isPaused = false;

    private State state;
    private Visualiser visualiser;

    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        this.state = new State(boardWidth/tileSize, boardHeight/tileSize, 4, 4); // todo
        this.visualiser = new Visualiser(tileSize); // todo

        setupBoard();

        // Loop game every N ms
        gameLoop.start();
    }

    private void setupBoard() {
        setPreferredSize(new Dimension(boardWidth + SCOREBOARD_WIDTH, boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMapBackground(g);
//         drawGrid(g);
        drawBlockedSquares(g);
        drawApples(g);
        drawPlayers(g);
        drawScoreboard(g);
    }

    private void drawMapBackground(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, boardWidth, boardHeight);
    }

    private void drawBlockedSquares(Graphics g) {
        for (Square square : state.getBoard().getBlockedSquares()) {
            visualiser.draw(g, Color.yellow, square);
        }
    }

    private void drawPlayers(Graphics g) {
        for (Player player : state.getPlayers()) {
            if (player.isAlive()) {
                visualiser.drawPlayer(g, getPlayerColor(player), player);
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

    private void drawScoreboard(Graphics g) {
        int scoreboardX = boardWidth;
        g.setColor(new Color(17, 24, 39));
        g.fillRect(scoreboardX, 0, SCOREBOARD_WIDTH, boardHeight);

        g.setFont(new Font("Arial", Font.PLAIN, 16));

        visualiser.drawState(g, state, scoreboardX + tileSize, tileSize + 16);

        for (Player player : state.getPlayers()) {
            visualiser.drawPlayerStats(g, getPlayerColor(player), player, scoreboardX + tileSize, tileSize + 25 + (22 * player.getId()));
        }
    }

    private Color getPlayerColor(Player player) {
        return PLAYER_COLORS[player.getId() % PLAYER_COLORS.length];
    }

    private void move() {
        if (isPaused) {
            return;
        }

        state.progress();
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

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                state.onKeyPress(Direction.LEFT);
                break;

            case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                state.onKeyPress(Direction.RIGHT);
                break;

            case KeyEvent.VK_UP, KeyEvent.VK_W:
                state.onKeyPress(Direction.UP);
                break;

            case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                state.onKeyPress(Direction.DOWN);
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
