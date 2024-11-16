package org.drachens.store;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.KyoriUtil.compBuild;

public abstract class StoreItem {
    private final String identifier;
    private final int cost;
    private final Material material;
    private final Component name;
    private Component description;
    private final Component canAfford = compBuild("Can afford",NamedTextColor.GREEN, TextDecoration.BOLD);
    private final Component cantAfford = compBuild("Cant afford",NamedTextColor.RED, TextDecoration.BOLD);
    private final Component purchased = compBuild("purchased",NamedTextColor.GREEN, TextDecoration.BOLD);
    public StoreItem(String identifier ,int cost, Material material, Component name){
        this.cost = cost;
        this.material = material;
        this.name = name;
        this.identifier = identifier;
    }
    public StoreItem(String identifier ,int cost, Material material, Component name, Component description){
        this.cost = cost;
        this.material = material;
        this.name = name;
        this.description = description;
        this.identifier = identifier;
    }
    public ItemStack getItem(CPlayer p){
        if (description!=null){
            return itemBuilder(material,name,getDescription(p));
        }
        return itemBuilder(material,name,getDescription(p));
    }
    public Component getDescription(CPlayer p){
        List<Component> comps = new ArrayList<>();
        comps.add(Component.text().append(Component.text("Costs: ")).append(Component.text(cost)).appendNewline().build());
        if (description!=null)
            comps.add(description);
        if (p.hasCosmetic(identifier)){
            comps.add(purchased);
        }else {
            if (p.getGems()>=cost){
                comps.add(canAfford);
            }else {
                comps.add(cantAfford);
            }
        }
        return Component.text().append(comps).build();
    }
    public boolean canBuy(CPlayer p){
        return p.getGems()>=cost;
    }
    public abstract void onPurchase(CPlayer p);
}
