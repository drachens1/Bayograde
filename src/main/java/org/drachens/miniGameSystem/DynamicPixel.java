package org.drachens.miniGameSystem;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.Material;

@Getter
@Setter
public class DynamicPixel {
    private final int weight;
    private Pixel location;
    private Material material;
    private Sprite sprite;

    public DynamicPixel(int weight, Material material) {
        this.weight = weight;
        this.material = material;
    }
    public int weight() {
        return weight;
    }

    public Material material() {
        return material;
    }
}
