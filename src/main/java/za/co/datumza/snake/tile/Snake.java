package za.co.datumza.snake.tile;

import java.awt.*;
import java.util.ArrayList;

public class Snake extends Tile {
    private int velocityX = 1;
    private int velocityY = 0;
    private final int size = 1;
    private final ArrayList<Tile> body = new ArrayList<>();


    public Snake(int x, int y) {
        super(x, y);

        for (int i = 0; i < size; i++) {
            if (velocityX != 0) {
                int next = x - (velocityX * i);
                body.add(new Tile(next, y));

            } else if (velocityY != 0) {
                int next = y - (velocityY * i);
                body.add(new Tile(x, next));
            }
        }
    }

    // returns boolean - is food eaten
    public boolean move(Food food) {
        // eat
        boolean hasEaten = isCollision(food);

        if (hasEaten) {
            body.add(new Tile(food));
        }

        moveBody();
        moveHead();

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
        super.draw(g, color);

        for (Tile bodyPart : body) {
            g.fillRect(bodyPart.convertX(), bodyPart.convertY(), tileSize, tileSize );
        }
    }

    public boolean isCollision(Tile tile) {
        return x== tile.x && y == tile.y;
    }
}
