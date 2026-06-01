package za.co.datumza.snake.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stats {
    private int score;
    private int bestScore;
    private int lives;
    private int length;
    private int longestLength;
    private int kills;
    private int mostKills;

    public Stats() {
        this.score = 0;
        this.bestScore = 0;
        this.lives = 0;
        this.length = 0;
        this.longestLength = 0;
        this.kills = 0;
        this.mostKills = 0;
    }

    public void eat() {
        ++this.score;
        ++this.length;

        this.bestScore = Math.max(this.score, this.bestScore);
        this.longestLength = Math.max(this.length, this.longestLength);
    }

    public void poison() {
        --this.score;
        this.length = Math.max(0, this.length - 1);
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
        return this.score + (this.kills * killsMultiplier);
    }

    public int getMaxScoreValue(int lengthMultiplier, int killsMultiplier) {
        return this.bestScore + (this.mostKills * killsMultiplier);
    }

    public String getScore(int lengthMultiplier, int killsMultiplier) {
        int score = getScoreValue(lengthMultiplier, killsMultiplier);
        int maxScore = getMaxScoreValue(lengthMultiplier, killsMultiplier);
        return String.format("%d %d %d %d %d %d", score, maxScore, this.length, this.longestLength, this.kills,this.mostKills);
    }
}
