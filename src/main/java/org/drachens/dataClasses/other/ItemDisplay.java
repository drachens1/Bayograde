package org.drachens.dataClasses.other;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import org.drachens.dataClasses.Provinces.Province;

import java.util.List;


public class ItemDisplay extends Entity {

    private final ItemStack itemStack;

    public ItemDisplay(ItemStack itemStack, Pos pos, Instance instance) {
        super(EntityType.ITEM_DISPLAY);
        this.itemStack = itemStack;
        setInstance(instance, pos);
        initializeDisplay();
        this.setInvisible(false);
    }
    public ItemDisplay(ItemStack itemStack, Pos pos, Instance instance, boolean invisible) {
        super(EntityType.ITEM_DISPLAY);
        this.itemStack = itemStack;
        setInstance(instance, pos);
        initializeDisplay();
        this.setInvisible(invisible);
    }
    public ItemDisplay(ItemStack itemStack, Pos pos, Instance instance, List<Player> viewers) {
        super(EntityType.ITEM_DISPLAY);
        this.itemStack = itemStack;
        setInstance(instance, pos);
        initializeDisplay();
        this.setInvisible(true);
        for (Player p : viewers){
            this.addViewer(p);
        }
    }

    public ItemDisplay(ItemStack itemStack, Province province) {
        super(EntityType.ITEM_DISPLAY);
        this.itemStack = itemStack;
        setInstance(province.getInstance(), province.getPos());
        initializeDisplay();
        this.setInvisible(false);
    }

    public ItemDisplay(ItemStack itemStack, Province province, boolean invisible) {
        super(EntityType.ITEM_DISPLAY);
        this.itemStack = itemStack;
        setInstance(province.getInstance(), province.getPos());
        initializeDisplay();
        this.setInvisible(invisible);
    }
    public ItemDisplay(ItemStack itemStack, Province province, List<Player> viewers) {
        super(EntityType.ITEM_DISPLAY);
        this.itemStack = itemStack;
        setInstance(province.getInstance(), province.getPos());
        initializeDisplay();
        this.setInvisible(true);
        for (Player p : viewers){
            this.addViewer(p);
        }
    }

    private void initializeDisplay() {
        this.setCustomNameVisible(false);
        this.setNoGravity(true);
        if (this.getEntityMeta() instanceof ItemDisplayMeta itemDisplay) {
            itemDisplay.setItemStack(itemStack);
        }
    }

    public void delete() {
        this.remove();
    }

    public void move(Pos pos) {
        this.teleport(pos);
    }

    public void move(Province province) {
        this.teleport(province.getPos());
    }
    public void setItem(ItemStack item){
        if (this.getEntityMeta() instanceof ItemDisplayMeta itemDisplay) {
            itemDisplay.setItemStack(item);
        }
    }
    public void hide(Player p){
        this.removeViewer(p);
    }
    public void show(Player p){
        this.addViewer(p);
    }
    public void invisible(Boolean visible){
        this.setInvisible(visible);
    }
}
