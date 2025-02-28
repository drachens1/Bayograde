package org.drachens.dataClasses.Diplomacy.Justifications;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countdown;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.events.countries.war.StartWarEvent;
import org.drachens.events.countries.warjustification.WarJustificationCompletionEvent;
import org.drachens.events.countries.warjustification.WarJustificationExpiresEvent;
import org.drachens.interfaces.Saveable;

public record WarJustification(Country from, Country against, Modifier modifier, int timeLeft, int expires, boolean declare_war, Instance instance) implements Saveable {
    public WarJustification{
        Countdown expiration = new Countdown(expires,()->{
            EventDispatcher.call(new WarJustificationExpiresEvent(this, from));
            from.getDiplomacy().removeCompletedWarJustification(against.getName());
        });
        new Countdown(timeLeft,()->{
            EventDispatcher.call(new WarJustificationCompletionEvent(this, from));
            expiration.start(instance);
            from.getDiplomacy().addCompletedWarJustification(against.getName(),this);
            from.getDiplomacy().removeWarJustification(against.getName());
            if (declare_war){
                EventDispatcher.call(new StartWarEvent(from, against, this));
            }
        }).start(instance);
    }
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("from",new JsonPrimitive(from.getName()));
        jsonObject.add("against",new JsonPrimitive(against.getName()));
        jsonObject.add("modifier",new JsonPrimitive(modifier.getIdentifier()));
        jsonObject.add("timeLeft",new JsonPrimitive(timeLeft));
        jsonObject.add("expires",new JsonPrimitive(expires));
        jsonObject.add("expires",new JsonPrimitive(declare_war));
        return jsonObject;
    }

//    public static WarJustification fromJson(JsonObject jsonObject, Instance instance){
//        Modifier modifier1 = new Modifier.create();
//        return new WarJustification(jsonObject.get("from").getAsString(),jsonObject.get("against").getAsString(),)
//    }
}
