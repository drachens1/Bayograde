package org.drachens.miniGameSystem;

@FunctionalInterface
public interface MiniGameRunnable {
    void run(Sprite collided);
}
