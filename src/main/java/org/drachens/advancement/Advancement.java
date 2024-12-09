package org.drachens.advancement;

import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.Material;
import org.drachens.interfaces.Event;

public record Advancement(String identifier, Material item, FrameType frameType,
                          int[] coords, Component title, Component description, String parent, float times, Class<? extends Event> event) {
}
