package dev.ng5m.util;

public abstract class Configurable<T extends Settings> {
    public T settings;

    public Configurable() {}
    public Configurable(T settings) {
        this.configure(settings);
    }

    public final void configure(T settings) {
        this.settings = settings;
    }
}
