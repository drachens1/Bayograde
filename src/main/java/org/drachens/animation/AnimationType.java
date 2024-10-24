package org.drachens.animation;

import org.drachens.dataClasses.other.ItemDisplay;

public interface AnimationType {
    void start(ItemDisplay itemDisplay, boolean repeat);
    void stop(ItemDisplay itemDisplay);
}
