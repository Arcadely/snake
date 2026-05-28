package za.co.datumza.snake.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tile {
    @Getter
    private static final int tileSize = 10;

    private int x;
    private int y;
}
