package org.drachens.dataClasses;

import lombok.Getter;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;

public class Countdown {
    private int timeLeft;
    @Getter
    private final Runnable runnable;
    public Countdown(int timeLeft, Runnable runnable){
        this.timeLeft=timeLeft;
        this.runnable=runnable;
    }
    public void start(Instance instance){
        ContinentalManagers.countdownManager.addCountDown(this,instance);
    }
    public int removeOne(){
        return timeLeft--;
    }
}
