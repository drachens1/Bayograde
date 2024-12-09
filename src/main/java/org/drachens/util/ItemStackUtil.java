package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.List;

public class ItemStackUtil {
    public static ItemStack itemBuilder(Material material) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .build();
    }

    public static ItemStack itemBuilder(Material material, Component name) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .customName(name)
                .build();
    }

    public static ItemStack itemBuilder(Material material, Component name, int modelData, List<Component> description) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .customName(name)
                .lore(description)
                .customModelData(modelData)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }

    public static ItemStack itemBuilder(Material material, Component name, List<Component> description) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .customName(name)
                .lore(description)
                .build();
    }

    public static ItemStack itemBuilder(Material material, Component name, int modelData) {
        return ItemStack.builder(material)
                .maxStackSize(1)
                .customModelData(modelData)
                .customName(name)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }

    public static ItemStack itemBuilder(Material material, int modelData) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .customModelData(modelData)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }

    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .customName(Component.text(name, colour))
                .build();
    }

    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour, int modelData) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .customName(Component.text(name, colour))
                .customModelData(modelData)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }

    public static ItemStack itemBuilder(Material material, String name, NamedTextColor colour, int modelData, List<Component> lore) {
        return ItemStack.of(material).builder()
                .maxStackSize(1)
                .customName(Component.text(name, colour))
                .customModelData(modelData)
                .lore(lore)
                .build().withTag(Tag.Integer("CustomModelData"), modelData);
    }
}
