package org.drachens.events.countries.warjustification;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.interfaces.Event;

public class WarJustificationCancelEvent extends Event {
    private final WarJustification warJustification;
    private final Country from;
    private final Country against;
    public WarJustificationCancelEvent(WarJustification warJustification, Country from) {
        super(from.getInstance());
        this.from=from;
        this.against=warJustification.getAgainstCountry();
        this.warJustification=warJustification;
    }

    public WarJustification getWarJustification() {
        return warJustification;
    }

    public Country getFrom() {
        return from;
    }

    public Country getAgainst() {
        return against;
    }
}
