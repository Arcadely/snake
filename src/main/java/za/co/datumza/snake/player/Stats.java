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

    public int getScoreValue(int lengthMultiplier, int killsMultiplier) {
        return (this.length * lengthMultiplier) + (this.kills * killsMultiplier);
    }

    public int getMaxScoreValue(int lengthMultiplier, int killsMultiplier) {
        return (this.longestLength * lengthMultiplier) + (this.mostKills * killsMultiplier);
    }

    public String getScore(int lengthMultiplier, int killsMultiplier) {
        int score = getScoreValue(lengthMultiplier, killsMultiplier);
        int maxScore = getMaxScoreValue(lengthMultiplier, killsMultiplier);
        return String.format("%d %d %d %d %d %d", score, maxScore, this.length, this.longestLength, this.kills,this.mostKills);
    }
}
