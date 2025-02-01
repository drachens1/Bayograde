package org.drachens.events;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;

public record NewDay(int day, int month, int year, Instance world) implements Event {
    @Override
    public String toString() {
        return day + "/" + month + "/" + year;
    }
}