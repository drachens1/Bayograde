package org.drachens.advancement;

import dev.ng5m.CPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.AdvancementRoot;
import net.minestom.server.advancements.AdvancementTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdvancementManager {
    private final List<AdvancementSection> advancementSections = new ArrayList<>();
    private final net.minestom.server.advancements.AdvancementManager advancementManager = MinecraftServer.getAdvancementManager();

    public void register(AdvancementSection advancementSection){
        advancementSections.add(advancementSection);
    }

    public void addPlayer(CPlayer p){
        advancementSections.forEach(advancementSection -> {
            HashMap<String, net.minestom.server.advancements.Advancement> advancementHashMap = new HashMap<>();
            AdvancementRoot advancementRoot = new AdvancementRoot(advancementSection.getTitle(),advancementSection.getDescription(),
                    advancementSection.getItem(), advancementSection.getFrameType(),0,0,"section");

            AdvancementTab advancementTab = advancementManager.createTab(advancementSection.getIdentifier(),advancementRoot);

            advancementSection.getAdvancements().forEach(advancement -> advancementHashMap.put(advancement.identifier(),
                    new net.minestom.server.advancements.Advancement(advancement.title(),advancement.description(),advancement.item(),
                            advancement.frameType(),advancement.coords()[0], advancement.coords()[1])));

            advancementSection.getAdvancements().forEach(advancement -> advancementTab.createAdvancement(advancement.identifier(),
                    advancementHashMap.get(advancement.identifier()), advancementHashMap.getOrDefault(advancement.parent(), advancementRoot)));
            advancementTab.addViewer(p);
        });
    }
}
