package dev.ng5m.util;

import java.util.Map;

public enum Direction {
    NONE(0, 0),
    NORTH(0, 1),
    EAST(-1, 0),
    SOUTH(0, -1),
    WEST(1, 0);

    public static final Map<Direction, Direction> opposites = Map.of(
            NORTH, SOUTH,
            EAST, WEST,
            SOUTH, NORTH,
            WEST, EAST,
            NONE, NONE
    );

    public final int x;
    public final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
