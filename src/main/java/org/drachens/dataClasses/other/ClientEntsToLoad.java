package org.drachens.dataClasses.other;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientEntsToLoad {
    private HashMap<Instance, List<Clientside>> toLoad = new HashMap<>();

    public void loadPlayer(Player p) {
        if (!toLoad.containsKey(p.getInstance())) return;
        for (Clientside ents : toLoad.get(p.getInstance())) {
            ents.addViewer(p);
        }
    }

    public void unloadPlayer(Player p) {
        if (!toLoad.containsKey(p.getInstance())) return;
        for (Clientside ents : toLoad.get(p.getInstance())) {
            ents.removeViewer(p);
        }
    }

    public void addClientSide(Instance instance, Clientside clientside) {
        toLoad.computeIfAbsent(instance, k -> new ArrayList<>());
        toLoad.get(instance).add(clientside);
    }

    public void removeClientSide(Instance instance, Clientside clientside) {
        if (!toLoad.containsKey(instance)) return;
        toLoad.get(instance).remove(clientside);
    }
    public List<Clientside> getClientSides(Instance instance){
        return toLoad.get(instance);

    }

    public void reset(){
        toLoad = new HashMap<>();
    }
}
