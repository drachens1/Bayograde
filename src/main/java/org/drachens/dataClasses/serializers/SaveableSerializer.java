package org.drachens.dataClasses.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.drachens.interfaces.Saveable;

import java.lang.reflect.Type;

public class SaveableSerializer implements JsonSerializer<Saveable> {
    @Override
    public JsonElement serialize(Saveable saveable, Type typeOfSrc, JsonSerializationContext context) {
        return saveable.toJson();
    }
}
