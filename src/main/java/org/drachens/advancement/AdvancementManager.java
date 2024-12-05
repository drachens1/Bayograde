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
                    advancementSection.getItem(), advancementSection.getFrameType(),0,0,"a");

            AdvancementTab advancementTab =advancementManager.createTab(p.getUuid()+advancementSection.getIdentifier(),advancementRoot);

            advancementSection.getAdvancements().forEach(advancement -> advancementHashMap.put(advancement.getIdentifier(),
                    new net.minestom.server.advancements.Advancement(advancement.getTitle(),advancement.getDescription(),advancement.getItem(),
                            advancement.getFrameType(),advancement.getCoords()[0], advancement.getCoords()[1])));

            advancementSection.getAdvancements().forEach(advancement -> {
                advancementTab.createAdvancement(advancement.getIdentifier(),
                        advancementHashMap.get(advancement.getIdentifier()), advancementHashMap.getOrDefault(advancement.getParent(), advancementRoot));
            });

            advancementTab.addViewer(p);
        });
    }
}
