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
    private int offsetX;
    private int offsetY;
    private boolean IS_ENDLESS = false;

    public Visualiser(int tileSize) {
        this.tileSize = tileSize;
    }

    public Visualiser(int tileSize, int offsetX, int offsetY) {
        this.tileSize = tileSize;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void draw(Graphics g, Color color, Square square) {
        g.setColor(color);
        g.fillRect(convertX(square.getX()), convertY(square.getY()), tileSize, tileSize);
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

    public void drawState(Graphics g, State state, int x, int y) {
        String info = IS_ENDLESS ? String.format("State: %d", state.getCurrentState()) : String.format("State: %d/%d", state.getCurrentState(), state.getMAX_STATES());

        g.setColor(Color.white);
        g.drawString(info, x, y);
    }

    public void drawPlayerStats(Graphics g, Color color, Player player, int x, int y) {
        Stats stats = player.getStats();
        String type = player.isZombie() ? "Zombie" : "Player";
        String info = String.format(
                "%s%d %s S:%d B:%d L:%d K:%d",
                player.isZombie() ? "Z" : "P",
                player.getId(),
                type,
                stats.getScore(),
                stats.getBestScore(),
                stats.getLength(),
                stats.getKills()
        );

        g.setColor(color);
        g.drawString(info, x, y);
    }

    public int convert(int value) {
        return value * tileSize;
    }

    public int convertX(int value) {
        return offsetX + convert(value);
    }

    public int convertY(int value) {
        return offsetY + convert(value);
    }
}
