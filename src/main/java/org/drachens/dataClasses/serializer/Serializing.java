package org.drachens.dataClasses.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.drachens.interfaces.Saveable;

import java.io.IOException;

public class Serializing extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof Saveable) {
            ObjectNode node = (ObjectNode) gen.getCodec().createObjectNode();
            gen.writeTree(node);
        } else {
            gen.writeNull();
        }
    }
}