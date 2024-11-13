package org.drachens.events;

import net.minestom.server.instance.Instance;
import org.drachens.interfaces.Event;

public class NewDay extends Event {
    private final int day;
    private final int year;
    private final int month;
    private final Instance world;
    private boolean cancelled;

    public NewDay(int day, int month, int year, Instance world) {
        this.day = day;
        this.year = year;
        this.month = month;
        this.cancelled = false;
        this.world = world;
    }

    public Instance getWorld() {
        return world;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}