package org.drachens.events;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.instance.Instance;

public class NewDay implements Event, CancellableEvent {
    private boolean cancelled;
    private final int day;
    private final int year;
    private final int month;
    private final int week;
    private final Instance world;

    public NewDay(int day, int week , int month, int year, Instance world) {
        this.day = day;
        this.year = year;
        this.month = month;
        this.cancelled = false;
        this.week = week;
        this.world = world;
    }

    public Instance getWorld(){return world;}

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getWeek(){
        return week;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}