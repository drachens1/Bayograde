package org.drachens.miniGameSystem;

public class RelativePos {
    public static final RelativePos ZERO = new RelativePos(0, 0);

    private final int x;
    private final int y;

    public RelativePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
