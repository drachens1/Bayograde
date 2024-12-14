package org.drachens.miniGameSystem;

import net.minestom.server.item.Material;

public class DynamicPixel {
    private final int weight;
    private final Material material;
    public DynamicPixel(int weight, Material material){
        this.weight=weight;
        this.material=material;
    }
    private Sprite sprite;
    public void setSprite(Sprite sprite){
        this.sprite=sprite;
    }
    public Sprite getSprite(){
        return sprite;
    }
    public int weight(){
        return weight;
    }
    public Material material(){
        return material;
    }
}
