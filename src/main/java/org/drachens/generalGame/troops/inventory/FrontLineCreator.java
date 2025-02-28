package org.drachens.generalGame.troops.inventory;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FrontLineCreator extends HotbarItemButton {
    public FrontLineCreator() {
        super(itemBuilder(Material.ORANGE_DYE, "Hi", NamedTextColor.GOLD));
    }


    @Override
    public void onLeftClickOnBlock(OnUse onUse) {

    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {

    }
}
