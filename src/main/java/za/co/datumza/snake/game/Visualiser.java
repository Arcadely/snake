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

    public void drawState(Graphics g, State state) {
        String info = IS_ENDLESS ? String.format("State: %d", state.getCurrentState()) : String.format("State: %d/%d", state.getCurrentState(), state.getMAX_STATES());

        g.setColor(Color.white);
        g.drawString(info, tileSize, tileSize + 16);
    }

    public void drawPlayerStats(Graphics g, Color color, Player player) {
        Stats stats = player.getStats();
        int score = (stats.getLength() * LENGTH_MULTIPLIER) + (stats.getKills() * KILL_MULTIPLIER);
        int maxScore = (stats.getLongestLength() * LENGTH_MULTIPLIER) + (stats.getMostKills() * KILL_MULTIPLIER);
        String info = IS_ENDLESS ? String.format("%s: %d", player.getId(), score) : String.format("%s (%d Lives): %d", player.getId(), stats.getLives(), maxScore);

        g.setColor(color);
        g.drawString(info, tileSize, tileSize + 25 + (22 * player.getId()));
    }

    public int convert(int value) {
        return value * tileSize;
    }
}
