package org.drachens.dataClasses.Diplomacy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countdown;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.events.countries.nonaggression.NonAggressionExpireEvent;
import org.drachens.interfaces.Saveable;

public record NonAggressionPact(Country from, Country to, int duration, Instance instance) implements Saveable {
    public NonAggressionPact {
        new Countdown(duration,()->{
            EventDispatcher.call(new NonAggressionExpireEvent(this));
        }).start(instance);
    }
    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("from",new JsonPrimitive(from.getName()));
        jsonObject.add("to",new JsonPrimitive(to.getName()));
        jsonObject.add("duration",new JsonPrimitive(duration));
        return jsonObject;
    }

//    public static NonAggressionPact fromJson(JsonObject jsonObject, Instance instance){
//        return new NonAggressionPact(jsonObject.get("from").getAsString(),jsonObject.get("to").getAsString(),jsonObject.get("duration").getAsInt(),instance);
//    }
}
