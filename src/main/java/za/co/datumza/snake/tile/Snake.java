package za.co.datumza.snake.tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Snake extends Tile {
    private ArrayList<Tile> body = new ArrayList<>();

    private int velocityX = 1;
    private int velocityY = 0;
    private  int size = 0;
    private int lives = 1;
    private boolean isAlive = true;

    private final boolean isEndless = true;
    private final int INITIAL_SIZE = 3;
    private final int boardWidth;
    private final int boardHeight;
    private final Random random;

    public Snake(int boardWidth, int boardHeight, Random random) {
        super(0, 0);

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.random = random;

        initialise();
    }

    // returns boolean - is food eaten
    public boolean move(Food food) {
        if (isAlive) {
            if (isBodyCollision(this) || isOutOfBounds()) {
                kill();
                return false;
            }

            boolean hasEaten = hasEaten(food);
            moveBody();
            moveHead();

            return hasEaten;
        };

        if (lives >= 1 || isEndless) {
            isAlive = true;
        }

        return false;
    }

    public void setVelocityX(int velocityX) {
        if (this.velocityX != 0) {
            return;
        }

        this.velocityX = velocityX;
        this.velocityY = 0;
    }

    public void setVelocityY(int velocityY) {
        if (this.velocityY != 0) {
            return;
        }

        this.velocityY = velocityY;
        this.velocityX = 0;
    }

    public void draw(Graphics g, Color color) {
        if (isAlive) {
            super.draw(g, color);

            for (Tile bodyPart : body) {
                g.fillRect(bodyPart.convertX(), bodyPart.convertY(), tileSize, tileSize );
            }
        }
    }

    public boolean isBodyCollision(Snake snake) {
        for (Tile bodyPart : snake.body) {
            if (isCollision(bodyPart)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCollision(Tile tile) {
        return x == tile.x && y == tile.y;
    }

    private void initialise() {
        body =  new ArrayList<>();
        size = INITIAL_SIZE;
        x = random.nextInt(boardWidth/tileSize);
        y = random.nextInt(boardHeight/tileSize);

        for (int i = 1; i < size; i++) {
            if (velocityX != 0) {
                int next = x - (velocityX * i);
                body.add(new Tile(next, y));

            } else if (velocityY != 0) {
                int next = y - (velocityY * i);
                body.add(new Tile(x, next));
            }
        }
    }

    private boolean hasEaten(Food food) {
        boolean hasEaten = isCollision(food);

        if (hasEaten) {
            body.add(new Tile(food));
        }
        return hasEaten;
    }

    private void moveHead() {
        this.x += velocityX;
        this.y += velocityY;
    }

    private void moveBody() {
        for (int i = body.size() -1; i >= 0; i--) {

            Tile bodyPart = body.get(i);
            if (i == 0) {
                bodyPart.x = x;
                bodyPart.y = y;
            } else {
                Tile prevPart = body.get(i-1);
                bodyPart.x = prevPart.x;
                bodyPart.y = prevPart.y;
            }
        }
    }

    private boolean isOutOfBounds() {
        int newX = x + velocityX;
        int newY = y + velocityY;

        return this.convert(newX) < 0 || this.convert(newY) < 0 || this.convert(newX) >= boardWidth || this.convert(newY) >= boardHeight ;
    }

    private void kill() {
        isAlive = false;
        --lives;
        this.initialise();
    }
}
