package org.drachens.interfaces.ai;

import org.drachens.dataClasses.Province;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AI {
    protected final List<Province> attackedAt = new CopyOnWriteArrayList<>();

    public void attackedAt(Province province){
        attackedAt.add(province);
    }
    public abstract void tick();
    public abstract void fasterTick();
}
