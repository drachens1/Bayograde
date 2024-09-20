package dev.ng5m;

import java.util.List;

public final class ImmutableList<T> {
    private final T[] elements;

    @SafeVarargs
    public ImmutableList(T ... elements) {
        this.elements = elements;
    }

    public ImmutableList(List<T> elements) {
        this.elements = (T[]) elements.toArray();
    }

    public T[] getElements() {
        return this.elements;
    }

}
