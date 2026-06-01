package za.co.datumza.snake.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@AllArgsConstructor
public enum Direction {
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3);

    private final int value;

    public static Direction fromValue(int value) {
        return switch (value) {
            case 0 -> UP;
            case 1 -> DOWN;
            case 2 -> LEFT;
            case 3 -> RIGHT;
            default -> throw new IllegalArgumentException("Unknown value: " + value);
        };
    }


    public static Direction random() {
        Direction[] values = Direction.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
