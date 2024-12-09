package org.drachens.advancement;

import dev.ng5m.CPlayer;

@FunctionalInterface
public interface AdvancementRunnable {
    boolean run(CPlayer p);
}
