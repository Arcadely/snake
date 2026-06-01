package za.co.datumza.snake.game;

import za.co.datumza.snake.board.Apple;
import za.co.datumza.snake.board.AppleType;
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
import java.util.Comparator;
import java.util.List;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    protected static final int tileSize = 10;
    private final int REFRESH = 100;
    private static final int PLAYER_COUNT = 5;
    private static final int ZOMBIE_COUNT = 4;
    private static final int APPLE_COUNT = 6;
    private static final int GAME_PADDING = 20;
    private static final int SCOREBOARD_GAP = 16;
    private static final int SCOREBOARD_WIDTH = 300;
    private static final int SCOREBOARD_PADDING = 16;
    private static final int SCOREBOARD_LINE_HEIGHT = 22;
    private static final int SCOREBOARD_SECTION_GAP = 18;
    private static final Color ZOMBIE_COLOR = new Color(156, 163, 175);
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

        this.state = new State(boardWidth/tileSize, boardHeight/tileSize, PLAYER_COUNT, ZOMBIE_COUNT, APPLE_COUNT); // todo
        this.visualiser = new Visualiser(tileSize, GAME_PADDING, GAME_PADDING); // todo

        setupBoard();

        // Loop game every N ms
        gameLoop.start();
    }

    private void setupBoard() {
        setPreferredSize(new Dimension(boardWidth + SCOREBOARD_WIDTH + SCOREBOARD_GAP + (GAME_PADDING * 2), boardHeight + (GAME_PADDING * 2)));
        setBackground(new Color(209, 213, 219));
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
        g.fillRect(GAME_PADDING, GAME_PADDING, boardWidth, boardHeight);
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
            Color color = apple.getType() == AppleType.NORMAL ? Color.green : new Color(220, 38, 38);
            visualiser.draw(g, color, apple.getPosition());
        }
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < boardWidth / tileSize; i++) {
            int gridOffset = GAME_PADDING + (i * tileSize);
            g.drawLine(gridOffset, GAME_PADDING, gridOffset, GAME_PADDING + boardHeight);
            g.drawLine(GAME_PADDING, gridOffset, GAME_PADDING + boardWidth, gridOffset);
        }
    }

    private void drawScoreboard(Graphics g) {
        int scoreboardX = GAME_PADDING + boardWidth + SCOREBOARD_GAP;
        int contentX = scoreboardX + SCOREBOARD_PADDING;
        int stateY = GAME_PADDING + SCOREBOARD_PADDING + 16;
        int firstScoreY = stateY + SCOREBOARD_SECTION_GAP + SCOREBOARD_LINE_HEIGHT;

        g.setColor(new Color(17, 24, 39));
        g.fillRect(scoreboardX, GAME_PADDING, SCOREBOARD_WIDTH, boardHeight);

        g.setFont(new Font("Arial", Font.PLAIN, 14));

        visualiser.drawState(g, state, contentX, stateY);

        List<Player> sortedPlayers = state.getPlayers().stream()
                .sorted(Comparator
                        .<Player>comparingInt(player -> player.getStats().getBestScore()).reversed()
                        .thenComparing(Comparator.comparingInt((Player player) -> player.getStats().getScore()).reversed())
                        .thenComparingInt(Player::getId))
                .toList();

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            visualiser.drawPlayerStats(
                        g,
                        getPlayerColor(player),
                        player,
                        contentX,
                        firstScoreY + (SCOREBOARD_LINE_HEIGHT * i)
            );
        }
    }

    private Color getPlayerColor(Player player) {
        if (player.isZombie()) {
            return ZOMBIE_COLOR;
        }

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
