package org.drachens.dataClasses.additional;

public class Purges implements EventsRunner {
    @Override
    public boolean newDay() {
        return false;
    }
}
