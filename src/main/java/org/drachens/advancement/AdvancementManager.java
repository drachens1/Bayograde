package org.drachens.advancement;

import dev.ng5m.CPlayer;
import kotlin.Pair;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.AdvancementRoot;
import net.minestom.server.advancements.AdvancementTab;
import net.minestom.server.advancements.notifications.Notification;
import net.minestom.server.advancements.notifications.NotificationCenter;
import org.drachens.events.AdvancementEvent;
import org.drachens.fileManagement.PlayerInfoEntry;

import java.util.*;

public class AdvancementManager {
    private final List<AdvancementSection> advancementSections = new ArrayList<>();
    private final net.minestom.server.advancements.AdvancementManager advancementManager = MinecraftServer.getAdvancementManager();

    private final HashMap<UUID, HashMap<String, Pair<Integer, List<Pair<Advancement, net.minestom.server.advancements.Advancement>>>>> playersAdvancementHashMap = new HashMap<>();

    public void register(AdvancementSection advancementSection) {
        advancementSections.add(advancementSection);
    }

    public void addPlayer(CPlayer p) {
        PlayerInfoEntry playerInfoEntry = p.getPlayerInfoEntry();
        HashMap<String, Integer> eventCountHashmap = playerInfoEntry.getEventAchievementTrigger();
        advancementSections.forEach(advancementSection -> {
            String name = advancementSection.getIdentifier() + p.getUuid();
            if (advancementManager.getTab(name) != null) {
                Objects.requireNonNull(advancementManager.getTab(name)).addViewer(p);
                return;
            }
            HashMap<String, net.minestom.server.advancements.Advancement> advancementHashMap = new HashMap<>();
            HashMap<String, Pair<Integer, List<Pair<Advancement, net.minestom.server.advancements.Advancement>>>> eventsLists = new HashMap<>();

            AdvancementRoot advancementRoot = new AdvancementRoot(advancementSection.getTitle(), advancementSection.getDescription(),
                    advancementSection.getItem(), advancementSection.getFrameType(), 0, 0, "nether.png");
            advancementRoot.setAchieved(true);

            AdvancementTab advancementTab = advancementManager.createTab(name, advancementRoot);

            advancementSection.getAdvancements().forEach(advancement -> advancementHashMap.put(advancement.identifier(),
                    new net.minestom.server.advancements.Advancement(advancement.title(), advancement.description(), advancement.item(),
                            advancement.frameType(), advancement.x(), advancement.y())));

            advancementSection.getAdvancements().forEach(advancement -> {
                net.minestom.server.advancements.Advancement advancement1 = advancementHashMap.get(advancement.identifier());
                advancementTab.createAdvancement(advancement.identifier(),
                        advancement1, advancementHashMap.getOrDefault(advancement.parent(), advancementRoot));

                if (eventCountHashmap.getOrDefault(advancement.event(), 0) > advancement.times()) {
                    advancement1.setAchieved(true);
                    return;
                }
                List<Pair<Advancement, net.minestom.server.advancements.Advancement>> events = eventsLists.getOrDefault(advancement.event(), new Pair<>(0, new ArrayList<>())).component2();
                events.add(new Pair<>(advancement, advancement1));
                eventsLists.put(advancement.event(), new Pair<>(playerInfoEntry.getEventAchievementTrigger().getOrDefault(advancement.event(), 0), events));
            });

            playersAdvancementHashMap.put(p.getUuid(), eventsLists);
            advancementTab.addViewer(p);
        });
    }

    public AdvancementManager() {
        MinecraftServer.getGlobalEventHandler().addListener(AdvancementEvent.class, e -> {
            CPlayer player = e.getPlayer();
            String eventName = e.getName();
            HashMap<String, Pair<Integer, List<Pair<Advancement, net.minestom.server.advancements.Advancement>>>> playerEvents = playersAdvancementHashMap.get(player.getUuid());
            if (playerEvents == null) return;
            Pair<Integer, List<Pair<Advancement, net.minestom.server.advancements.Advancement>>> pair = playerEvents.get(eventName);
            if (pair == null) return;
            int count = pair.component1() + 1;
            player.getPlayerInfoEntry().addAchievementEventTriggered(eventName, count);
            List<Pair<Advancement, net.minestom.server.advancements.Advancement>> advancements = pair.component2();
            playerEvents.put(eventName, new Pair<>(count, advancements));
            advancements.removeIf(advancement -> {
                if (count >= advancement.component1().times()) {
                    Advancement displayAdv = advancement.component1();
                    advancement.component2().setAchieved(true);
                    Notification notification = new Notification(
                            displayAdv.title(),
                            displayAdv.frameType(),
                            displayAdv.item()
                    );
                    NotificationCenter.send(notification, player);
                    return true;
                }
                return false;
            });

            playersAdvancementHashMap.put(player.getUuid(), playerEvents);
        });
    }
}
