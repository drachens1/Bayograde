package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class ItemStackUtil {
    public static ItemStack itemBuilder(Material material){
        return ItemStack.of(material).builder()
                .build();
    }
    public static ItemStack itemBuilder(Material material, int modelData){
        return ItemStack.of(material).builder()
                .customModelData(modelData)
                .build();
    }
    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour){
        return ItemStack.of(material).builder()
                .customName(compBuild(name, colour))
                .build();
    }

    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour, int itemMeta){
        return ItemStack.of(material).builder()
                .customName(compBuild(name, colour))
                .customModelData(itemMeta)
                .build();
    }

    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour, int itemMeta, List<Component> lore){
        return ItemStack.of(material).builder()
                .customName(compBuild(name,colour))
                .customModelData(itemMeta)
                .lore(lore)
                .build();
    }
}
