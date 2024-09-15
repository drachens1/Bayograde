package org.drachens.dataClasses.Economics;

import org.drachens.dataClasses.Countries.Country;

public interface PlaceableBuilds {
    void onCaptured(Country capturer);

    void onBombed(float dmg);

    void onDestroy();

    void onUpgrade(int amount);
}
