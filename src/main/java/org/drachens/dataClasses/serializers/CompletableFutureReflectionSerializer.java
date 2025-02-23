package org.drachens.dataClasses.serializers;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureReflectionSerializer implements JsonSerializer<CompletableFuture<?>>, JsonDeserializer<CompletableFuture<?>> {
    @Override
    public JsonElement serialize(CompletableFuture<?> src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            Field field = CompletableFuture.class.getDeclaredField("result");
            field.setAccessible(true);
            Object result = field.get(src);
            return context.serialize(result);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new JsonIOException("Failed to access 'result' field", e);
        }
    }

    @Override
    public CompletableFuture<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Object result = context.deserialize(json, Object.class);
        CompletableFuture<Object> future = new CompletableFuture<>();
        future.complete(result);
        return future;
    }
}
