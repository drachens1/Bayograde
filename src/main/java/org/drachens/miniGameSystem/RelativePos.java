package org.drachens.miniGameSystem;

public record RelativePos(int x, int y) {
    public static final RelativePos ZERO = new RelativePos(0, 0);

    public String toString() {
        return x + ", " + y;
    }
}
