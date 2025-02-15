package org.drachens.dataClasses.additional;

import com.google.gson.JsonElement;
import org.drachens.interfaces.Saveable;

public class Purges implements EventsRunner, Saveable {
    @Override
    public boolean newDay() {
        return false;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
