package org.drachens.dataClasses.Diplomacy;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.demands.DemandAcceptedEvent;
import org.drachens.events.demands.DemandCompletionEvent;
import org.drachens.events.demands.DemandDeniedEvent;

public abstract class Demand {
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
        EventDispatcher.call(new DemandDeniedEvent(this, fromCountry, toCountry));
        ifDenied();
    }

    public void accepted() {
        EventDispatcher.call(new DemandAcceptedEvent(this, fromCountry, toCountry));
        ifAccepted();
    }

    protected abstract void ifAccepted();

    protected abstract void ifDenied();

    public void complete() {
        EventDispatcher.call(new DemandCompletionEvent(this, fromCountry, toCountry));
        toCountry.sendDemand(this);
        onCompleted();
    }

    protected abstract void onCompleted();
    public abstract void copyButOpposite(Demand demand);
}
