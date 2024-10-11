package dev.ng5m.util;

public interface Hookable<R, T extends Settings> {

    R hook(T settings);

}
