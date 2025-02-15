package org.drachens.dataClasses.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class SerializerModule extends SimpleModule {
    public SerializerModule() {
        this.addSerializer(Serializing.class, new Serializing());
    }
}
