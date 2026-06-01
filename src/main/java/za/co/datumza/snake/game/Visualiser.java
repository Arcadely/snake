package za.co.datumza.snake.game;

import lombok.AllArgsConstructor;
import za.co.datumza.snake.board.Square;
import za.co.datumza.snake.board.State;
import za.co.datumza.snake.player.Player;
import za.co.datumza.snake.player.Stats;

import java.awt.*;

@AllArgsConstructor
public class Visualiser {
    private int tileSize;
    private boolean IS_ENDLESS = false;
    private int LENGTH_MULTIPLIER = 5;
    private int KILL_MULTIPLIER = 10;

    public Visualiser(int tileSize) {
        this.tileSize = tileSize;
    }

    public void draw(Graphics g, Color color, Square square) {
        g.setColor(color);
        g.fillRect(convert(square.getX()), convert(square.getY()), tileSize, tileSize);
    }

    public void drawPlayer(Graphics g, Color color, Player player) {
        draw(g, color, player.getHead());
        for (Square square : player.getBody()) {
            draw(g, color, square);
        }
    }

    public void drawState(Graphics g, State state) {
        String info = IS_ENDLESS ? String.format("State: %d", state.getCurrentState()) : String.format("State: %d/%d", state.getCurrentState(), state.getMAX_STATES());

        g.setColor(Color.white);
        g.drawString(info, tileSize, tileSize + 16);
    }

    public void drawPlayerStats(Graphics g, Color color, Player player) {
        Stats stats = player.getStats();
        String score = stats.getScore(LENGTH_MULTIPLIER, KILL_MULTIPLIER);
        String info = IS_ENDLESS ? String.format("%s: %s", player.getId(), score) : String.format("%s (%d Lives): %s", player.getId(), stats.getLives(), score);

        g.setColor(color);
        g.drawString(info, tileSize, tileSize + 25 + (22 * player.getId()));
    }

    public int convert(int value) {
        return value * tileSize;
    }
}
