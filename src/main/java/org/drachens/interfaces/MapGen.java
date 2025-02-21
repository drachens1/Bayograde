package org.drachens.interfaces;

import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.VotingOption;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.OtherUtil.runThread;

public abstract class MapGen {
    private final List<Instance> generating = new ArrayList<>();
    private final int sizeX;
    private final int sizeY;

    protected MapGen(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public abstract void onGenerate(Instance instance, VotingOption votingOption);

    public void generate(Instance instance, VotingOption votingOption){
        runThread(()-> onGenerate(instance,votingOption));
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void addGenerating(Instance instance) {
        this.generating.add(instance);
    }

    public void removeGenerating(Instance instance) {
        if (generating.contains(instance)) this.generating.remove(instance);
    }

    public boolean isGenerating(Instance instance) {
        return generating.contains(instance);
    }
}
