package org.drachens.dataClasses.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minestom.server.item.Material;

import java.lang.reflect.Type;

public class MaterialSerializer implements JsonSerializer<Material> {
    @Override
    public JsonElement serialize(Material material, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(material.name());
    }
}