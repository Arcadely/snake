package za.co.datumza.snake.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stats {
    private int LENGTH_MULTIPLIER = 1;
    private int KILL_MULTIPLIER = 3;

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
        ++this.length;
        this.longestLength = Math.max(this.length, this.longestLength);
        calculateScore();
    }

    public void poison() {
        this.length = Math.max(0, this.length - 1);
        this.score = (this.length * LENGTH_MULTIPLIER) + (this.kills * KILL_MULTIPLIER);
    }

    public void die() {
        this.score = 0;
        this.length = 0;
        this.kills = 0;
        --this.lives;
    }

    public void kill() {
        ++this.kills;
        this.mostKills = Math.max(this.mostKills, this.kills);
        calculateScore();
    }

    public String getScoreString() {
        return String.format("%d %d %d %d %d %d", this.score, this.bestScore, this.length, this.longestLength, this.kills,this.mostKills);
    }

    private void calculateScore() {
        this.score = (this.length * LENGTH_MULTIPLIER) + (this.kills * KILL_MULTIPLIER);
        this.bestScore = Math.max(this.bestScore, this.score);
    }
}
