package org.drachens.temporary;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.demand.Demand;

public class WW2Demands extends Demand {
    public WW2Demands(Country from, Country to) {
        super(from, to);
    }

    @Override
    public Component description() {
        return null;
    }

    @Override
    public void ifAccepted() {

    }

    @Override
    public void ifDenied() {

    }

    @Override
    protected void onCompleted() {

    }
}
