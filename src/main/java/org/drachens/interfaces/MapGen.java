package org.drachens.interfaces;

import lombok.Getter;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.VotingOption;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.OtherUtil.runThread;

public abstract class MapGen {
    private final List<Instance> generating = new ArrayList<>();
    @Getter
    private final int sizeX;
    @Getter
    private final int sizeY;

    protected MapGen(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public abstract void onGenerate(Instance instance, VotingOption votingOption);

    public void generate(Instance instance, VotingOption votingOption){
        runThread(()-> onGenerate(instance,votingOption));
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
