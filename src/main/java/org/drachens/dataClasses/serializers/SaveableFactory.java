package org.drachens.dataClasses.serializers;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.drachens.interfaces.Saveable;

import java.io.IOException;

public class SaveableFactory<T> implements TypeAdapterFactory {
    private final Class<T> baseType;

    public SaveableFactory(Class<T> baseType) {
        this.baseType = baseType;
    }

    @Override
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (!baseType.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final TypeAdapter<R> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, R value) throws IOException {
                if (value instanceof Saveable) {
                    out.jsonValue(((Saveable) value).toJson().toString());
                } else {
                    delegate.write(out, value);
                }
            }

            @Override
            public R read(JsonReader in) throws IOException {
                return delegate.read(in);
            }
        };
    }
}
