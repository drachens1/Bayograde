package org.drachens.miniGameSystem;

import net.minestom.server.item.Material;

public class DynamicPixel {
    private final int weight;
    private Pixel location;
    private Material material;
    private Sprite sprite;

    public DynamicPixel(int weight, Material material) {
        this.weight = weight;
        this.material = material;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int weight() {
        return weight;
    }

    public Material material() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setLocation(Pixel pixel) {
        this.location = pixel;
    }
}
