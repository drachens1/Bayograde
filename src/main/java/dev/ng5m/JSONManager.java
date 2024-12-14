package dev.ng5m;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JSONManager<T> {
    public final File file;

    public JSONManager(File file) {
        this.file = file;

        if (!file.exists()) {
            try {
                file.createNewFile();
                writeString("{}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected final JsonObject getRoot() {
        String read = Util.readFile(file.toPath());
        JsonElement parser = JsonParser.parseString(read);

        return parser.getAsJsonObject();
    }

    protected final void writeString(String s) {
        try {
            var writer = new FileWriter(file);
            writer.write(s);
            writer.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    protected final void write(JsonObject root) {
        var serialized = root.toString();

        writeString(serialized);
    }

    protected final JsonObject removeEntry(T t) {
        var root = getRoot();

        if (root.has(t.toString()))
            root.remove(t.toString());

        write(root);
        return root;
    }

    protected final void set(T t, JsonPrimitive p) {
        var root = removeEntry(t);

        root.add(t.toString(), p);

        write(root);
    }

    public void set(T t, String s) {
        set(t, new JsonPrimitive(s));
    }

    public void set(T t, Character c) {
        set(t, new JsonPrimitive(c));
    }

    public void set(T t, Number number) {
        set(t, new JsonPrimitive(number));
    }

    public void set(T t, Boolean bool) {
        set(t, new JsonPrimitive(bool));
    }

    public abstract boolean is(T t);
}
