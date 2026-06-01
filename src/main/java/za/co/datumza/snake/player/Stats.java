package za.co.datumza.snake.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stats {
    private final int LENGTH = 2;

    private int lives;
    private int length;
    private int longestLength;
    private int kills;
    private int mostKills;

    public Stats() {
        this.lives = 0;
        this.length = 0;
        this.longestLength = 0;
        this.kills = 0;
        this.mostKills = 0;
    }

    public void eat() {
        ++this.length;

        this.longestLength = Math.max(this.length, this.longestLength);
    }

    public void die() {
        this.length = LENGTH;
        this.kills = 0;
        --this.lives;
    }

    public void kill() {
        ++this.kills;

        this.mostKills = Math.max(this.mostKills, this.kills);
    }
}
