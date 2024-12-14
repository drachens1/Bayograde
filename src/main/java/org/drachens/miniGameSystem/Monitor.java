package org.drachens.miniGameSystem;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;

public class Monitor {
    private final Instance instance;
    private final Map<Pos, Pixel> pixelHashMap = new HashMap<>();
    private final AStarPathfinderMiniGame aStarPathfinder = new AStarPathfinderMiniGame();

    public Monitor(Instance instance) {
        this.instance = instance;
    }

    public void addPixel(Pos pos, Pixel pixel) {
        pixelHashMap.put(pos, pixel);
        instance.setBlock(pos, pixel.getMaterial().block());
    }

    public void addDynamicPixel(Pos pos, DynamicPixel dynamicPixel) {
        Pixel pixel = pixelHashMap.get(pos);
        if (pixel != null) {
            pixel.add(dynamicPixel);
        }
    }

    public void removeDynamicPixel(Pos pos, DynamicPixel dynamicPixel) {
        Pixel pixel = pixelHashMap.get(pos);
        if (pixel != null) {
            pixel.remove(dynamicPixel);
        }
    }

    public void moveDynamicPixel(Pos prev, Pos next, DynamicPixel dynamicPixel) {
        if (pixelHashMap.containsKey(prev) && pixelHashMap.containsKey(next)) {
            removeDynamicPixel(prev, dynamicPixel);
            addDynamicPixel(next, dynamicPixel);
        }
    }

    public void clear(Material newMat){
        pixelHashMap.forEach((pos, pixel) -> {
            pixel.setDefaultMaterial(newMat);
            pixel.clear();
        });
    }

    public Instance getInstance() {
        return instance;
    }

    public AStarPathfinderMiniGame getAStarPathfinder() {
        return aStarPathfinder;
    }
}


