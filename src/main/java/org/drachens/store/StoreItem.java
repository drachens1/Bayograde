package org.drachens.store;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.KyoriUtil.compBuild;
import static org.drachens.util.KyoriUtil.getPrefixes;

public abstract class StoreItem {
    private final Component boughtMessage;
    private final String identifier;
    private final int cost;
    private final Material material;
    private final Component name;
    private final Component description;
    private final int modelData;
    private final Component canAfford = compBuild("Can afford", NamedTextColor.GREEN, TextDecoration.BOLD);
    private final Component cantAfford = compBuild("Cant afford", NamedTextColor.RED, TextDecoration.BOLD);
    private final Component purchased = compBuild("purchased", NamedTextColor.GREEN, TextDecoration.BOLD);

    public StoreItem(String identifier, int cost, Material material, Component name, int modelData) {
        this.cost = cost;
        this.material = material;
        this.name = name;
        this.identifier = identifier;
        this.modelData = modelData;
        this.description = null;
        boughtMessage = Component.text()
                .append(getPrefixes("system"))
                .append(Component.text("You have successfully bought "))
                .append(name)
                .append(Component.text(" for "))
                .append(Component.text(cost))
                .build();
    }

    public StoreItem(String identifier, int cost, Material material, Component name, Component description, int modelData) {
        this.cost = cost;
        this.material = material;
        this.name = name;
        this.description = description;
        this.identifier = identifier;
        this.modelData = modelData;
        boughtMessage = Component.text()
                .append(getPrefixes("system"))
                .append(Component.text("You have successfully bought "))
                .append(name)
                .append(Component.text(" for "))
                .append(Component.text(cost))
                .build();
    }

    public ItemStack getItem(CPlayer p) {
        if (description != null) {
            return itemBuilder(material, name, modelData, getDescription(p));
        }
        return itemBuilder(material, name, modelData, getDescription(p));
    }

    public ItemStack getBoughtItem(CPlayer p) {
        return itemBuilder(material, name, modelData, getBoughtDescription(p));
    }

    public List<Component> getBoughtDescription(CPlayer p) {
        List<Component> comps = new ArrayList<>();
        if (description != null)
            comps.add(description);
        return comps;
    }

    public List<Component> getDescription(CPlayer p) {
        List<Component> comps = new ArrayList<>();
        comps.add(Component.text().append(Component.text("Costs: ")).append(Component.text(cost)).appendNewline().build());
        if (description != null)
            comps.add(description);
        if (p.hasCosmetic(identifier)) {
            comps.add(purchased);
        } else {
            if (p.getGold() >= cost) {
                comps.add(canAfford);
            } else {
                comps.add(cantAfford);
            }
        }
        return comps;
    }

    public boolean canBuy(CPlayer p) {
        return p.getGold() >= cost;
    }

    public void purchase(CPlayer p) {
        p.sendMessage(boughtMessage);
        p.minusGold(cost);
        ContinentalManagers.cosmeticsManager.addCosmetic(p, identifier);
        onPurchase(p);
    }

    protected abstract void onPurchase(CPlayer p);

    public void clickAfterBought(CPlayer p) {
        p.sendMessage(Component.text()
                .append(getPrefixes("system"))
                .append(Component.text("Equipped ", NamedTextColor.GREEN))
                .append(name)
                .build());
        onClickAfterBought(p);
    }

    protected abstract void onClickAfterBought(CPlayer p);

    public String getIdentifier() {
        return identifier;
    }
}
