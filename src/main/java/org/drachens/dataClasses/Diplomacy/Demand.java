package org.drachens.dataClasses.Diplomacy;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.countries.demands.DemandCompletionEvent;
import org.drachens.interfaces.Saveable;

public abstract class Demand implements Saveable {
    private final Country fromCountry;
    private final Country toCountry;

    public Demand(Country fromCountry, Country toCountry) {
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
    }

    public Country getFromCountry() {
        return fromCountry;
    }

    public Country getToCountry() {
        return toCountry;
    }

    public abstract Component description();

    public void denied() {
        ifDenied();
    }

    public void accepted() {
        ifAccepted();
    }

    protected abstract void ifAccepted();

    protected abstract void ifDenied();

    public void runCompleteEvent() {
        EventDispatcher.call(new DemandCompletionEvent(this, fromCountry, toCountry));
    }

    public void complete() {
        toCountry.sendDemand(this);
        onCompleted();
    }

    protected abstract void onCompleted();

    public abstract void copyButOpposite(Demand demand);


}
