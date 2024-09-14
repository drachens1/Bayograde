package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class ItemStackUtil {
    public static ItemStack itemBuilder(Material material){
        return ItemStack.of(material).builder()
                .build();
    }
    public static ItemStack itemBuilder(Material material, Component name){
        return ItemStack.of(material).builder()
                .customName(name)
                .build();
    }
    public static ItemStack itemBuilder(Material material, Component name, int modelData){
        return ItemStack.builder(material)
                .customModelData(modelData)
                .customName(name)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }
    public static ItemStack itemBuilder(Material material, int modelData){
        return ItemStack.of(material).builder()
                .customModelData(modelData)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }
    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour){
        return ItemStack.of(material).builder()
                .customName(compBuild(name, colour))
                .build();
    }

    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour, int modelData){
        return ItemStack.of(material).builder()
                .customName(compBuild(name, colour))
                .customModelData(modelData)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }

    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour, int modelData, List<Component> lore){
        return ItemStack.of(material).builder()
                .customName(compBuild(name,colour))
                .customModelData(modelData)
                .lore(lore)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }
}
