package org.drachens.dataClasses.Diplomacy;

import com.google.gson.JsonElement;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countdown;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.countries.nonaggression.NonAggressionExpireEvent;
import org.drachens.interfaces.Saveable;

public record NonAggressionPact(Country from, Country to, int duration) implements Saveable {
    public NonAggressionPact {
        new Countdown(duration,()->{
            EventDispatcher.call(new NonAggressionExpireEvent(this));
        }).start(from().getInstance());
    }
    @Override
    public JsonElement toJson() {
        return null;
    }
}
