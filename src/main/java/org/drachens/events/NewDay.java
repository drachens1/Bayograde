package org.drachens.events;

import net.minestom.server.instance.Instance;
import org.drachens.interfaces.Event;

public class NewDay extends Event {
    private final int day;
    private final int year;
    private final int month;
    private final Instance world;

    public NewDay(int day, int month, int year, Instance world) {
        super(world);
        this.day = day;
        this.year = year;
        this.month = month;
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

    @Override
    public String toString(){
        return day+"/"+month+"/"+year;
    }
}