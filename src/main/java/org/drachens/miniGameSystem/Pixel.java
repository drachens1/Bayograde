package org.drachens.miniGameSystem;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;

import java.util.LinkedList;

public class Pixel {
    private final Monitor monitor;
    private final Pos pos;
    private final LinkedList<DynamicPixel> dynamicPixels = new LinkedList<>();
    private Material material;

    public Pixel(Material material, Pos pos, Monitor monitor) {
        this.material = material;
        this.pos = pos;
        this.monitor = monitor;
        monitor.addPixel(pos, this);
    }

    public void add(DynamicPixel dynamicPixel) {
        if (!dynamicPixels.isEmpty()){
            Sprite sprite = dynamicPixel.getSprite();
            dynamicPixels.forEach(dynamicPixel1 -> {
                dynamicPixel1.getSprite().onCollision(sprite,pos);
                sprite.onCollision(dynamicPixel1.getSprite(),pos);
            });

        }
        if (dynamicPixels.isEmpty() || dynamicPixels.getFirst().weight() <= dynamicPixel.weight()) {
            dynamicPixels.addFirst(dynamicPixel);
        } else {
            dynamicPixels.add(dynamicPixel);
        }
        setMaterial(dynamicPixel.material());
    }

    public void remove(DynamicPixel dynamicPixel) {
        if (dynamicPixels.remove(dynamicPixel)) {
            if (dynamicPixels.isEmpty()) {
                setMaterial(monitor.getDefaultMaterial());
            } else {
                setMaterial(dynamicPixels.getFirst().material());
            }
        }
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
        monitor.getInstance().setBlock(pos, material.block());
    }

    public void clear(){
        dynamicPixels.clear();
    }
}