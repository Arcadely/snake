package za.co.datumza.snake.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stats {
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
        this.length = 0;
        this.kills = 0;
        --this.lives;
    }

    public void kill() {
        ++this.kills;

        this.mostKills = Math.max(this.mostKills, this.kills);
    }

    public String getScore(int lengthMultiplier, int killsMultiplier) {
        int score = (this.length * lengthMultiplier) + (this.kills * killsMultiplier);
        int maxScore = (this.longestLength * lengthMultiplier) + (this.mostKills * killsMultiplier);
        return String.format("%d %d %d %d %d %d", score, maxScore, this.length, this.kills, this.longestLength * lengthMultiplier, this.mostKills * killsMultiplier);
    }
}
