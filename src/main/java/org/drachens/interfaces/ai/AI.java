package org.drachens.interfaces.ai;

import org.drachens.dataClasses.Province;

import java.util.ArrayList;
import java.util.List;

public abstract class AI {
    protected final List<Province> attackedAt = new ArrayList<>();

    public void attackedAt(Province province){
        attackedAt.add(province);
    }
    public abstract void tick();
    public abstract void fasterTick();
}
