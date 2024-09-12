package org.drachens.dataClasses.Economics;

import org.drachens.dataClasses.Countries.Country;

public interface PlaceableBuilds {
    public void onCaptured(Country capturer);
    public void onBombed(float dmg);
    public void onDestroy();
    public void onConstruct();
    public void onUpgrade(int amount);
}
