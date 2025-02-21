package org.drachens.store;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public abstract class StoreItem {
    @Getter
    private final String identifier;
    private final int cost;
    private final Material material;
    private final Component name;
    private final Component description;
    private final int modelData;
    private final Component purchased = Component.text("purchased", NamedTextColor.GREEN, TextDecoration.BOLD);

    protected StoreItem(String identifier, int cost, Material material, Component name, int modelData) {
        this.cost = cost;
        this.material = material;
        this.name = name;
        this.identifier = identifier;
        this.modelData = modelData;
        this.description = null;
    }

    public ItemStack getItem(CPlayer p) {
        return itemBuilder(material, name, modelData, getDescription(p));
    }

    public ItemStack getBoughtItem() {
        return itemBuilder(material, name, modelData, getBoughtDescription());
    }

    public List<Component> getBoughtDescription() {
        List<Component> comps = new ArrayList<>();
        if (null != this.description) comps.add(description);
        return comps;
    }

    public List<Component> getDescription(CPlayer p) {
        List<Component> comps = new ArrayList<>();
        comps.add(Component.text().append(Component.text("Costs: ")).append(Component.text(cost)).appendNewline().build());
        if (null != this.description) comps.add(description);
        if (p.hasCosmetic(identifier)) {
            comps.add(purchased);
        }
        return comps;
    }

    public void clickAfterBought(CPlayer p) {
        p.sendMessage(Component.text()
                .append(MessageEnum.system.getComponent())
                .append(Component.text("Equipped ", NamedTextColor.GREEN))
                .append(name)
                .build());
        onClickAfterBought(p);
    }

    protected abstract void onClickAfterBought(CPlayer p);
}
