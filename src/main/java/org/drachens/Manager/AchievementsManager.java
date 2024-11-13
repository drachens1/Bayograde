package org.drachens.Manager;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.*;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

import static org.drachens.util.KyoriUtil.compBuild;

public class AchievementsManager {
    private AdvancementTab main;
    private AdvancementTab starterAdv;

    public AchievementsManager() {
        createAdvancements();
    }

    public void createAdvancements() {
        AdvancementManager advancementManager = MinecraftServer.getAdvancementManager();
        AdvancementRoot starterAdvRoot = new AdvancementRoot(
                compBuild("Starter_Advancements", NamedTextColor.GREEN),
                compBuild("Contains all the Starter Advancements for continentalMC", NamedTextColor.GREEN),
                Material.EMERALD, FrameType.GOAL, 0, 0, "a"
        );
        starterAdv = advancementManager.createTab("starter_advancements", starterAdvRoot);

        Advancement advancement = new Advancement(
                compBuild("Send your first message", NamedTextColor.GREEN),
                compBuild("Just send your first message to complete", NamedTextColor.GREEN),
                Material.PAPER, FrameType.GOAL, 1, 0
        );
        starterAdv.createAdvancement("firstmsg", advancement, starterAdvRoot);

        AdvancementRoot mainRoot = new AdvancementRoot(
                compBuild("Main_Advancements", NamedTextColor.GREEN),
                compBuild("Contains all the Main advancements for continentalMC", NamedTextColor.GRAY),
                Material.EMERALD, FrameType.GOAL, 0, 0, "nether"
        );
        main = MinecraftServer.getAdvancementManager().createTab("main_advancements", mainRoot);
    }

    public void addPlayerToAdv(Player p) {
        main.addViewer(p);
        starterAdv.addViewer(p);
    }

    public void removePlayerFromAdv(Player p) {
        main.removeViewer(p);
        starterAdv.removeViewer(p);
    }
}
