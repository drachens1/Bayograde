package org.drachens.dataClasses.Diplomacy.Justifications;

import com.google.gson.JsonElement;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countdown;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.events.countries.warjustification.WarJustificationCompletionEvent;
import org.drachens.events.countries.warjustification.WarJustificationExpiresEvent;
import org.drachens.interfaces.Saveable;

import java.util.function.Consumer;

public record WarJustification(Country from, Country against, Modifier modifier, int timeLeft, int expires, Consumer<WarJustification> runnable) implements Saveable {
    public WarJustification{
        Countdown expiration = new Countdown(expires,()->{
            EventDispatcher.call(new WarJustificationCompletionEvent(this, from));
            if (null != runnable) {
                runnable.accept(this);
            }
            from.getDiplomacy().removeCompletedWarJustification(against.getName());
        });
        new Countdown(timeLeft,()->{
            EventDispatcher.call(new WarJustificationExpiresEvent(this, from));
            expiration.start(from.getInstance());
            from.getDiplomacy().removeWarJustification(against.getName());
        }).start(from.getInstance());
    }
    @Override
    public JsonElement toJson() {
        return null;
    }
}
