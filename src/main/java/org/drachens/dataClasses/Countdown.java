package org.drachens.dataClasses;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;

public class Countdown {
    private int timeLeft;
    private final Runnable runnable;
    public Countdown(int timeLeft, Runnable runnable){
        this.timeLeft=timeLeft;
        this.runnable=runnable;
    }
    public void start(Instance instance){
        ContinentalManagers.countdownManager.addCountDown(this,instance);
    }
    public int removeOne(){
        timeLeft--;
        return timeLeft;
    }
    public Runnable getRunnable(){
        return runnable;
    }
}
