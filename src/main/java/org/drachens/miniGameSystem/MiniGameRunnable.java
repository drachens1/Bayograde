package org.drachens.miniGameSystem;

import net.minestom.server.coordinate.Pos;

@FunctionalInterface
public interface MiniGameRunnable {
    void run(Sprite collided, Pos pos);
}
