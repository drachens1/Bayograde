package org.drachens.temporary.worlds;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.World;
import org.drachens.temporary.scoreboards.DefaultScoreboard;

public class ContinentalWorld extends World {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;

    public ContinentalWorld() {
        super(MinecraftServer.getInstanceManager().createInstanceContainer(),new Pos(0,1,0));
        for (int x = -50; x < 50; x++){
            for (int z = -50; z < 50; z++){
                getInstance().loadChunk(x,z);
                getInstance().setBlock(x,0,z,Block.LAPIS_BLOCK,false);
            }
        }
    }

    @Override
    public void addPlayer(CPlayer p) {
        Instance instance = p.getInstance();
        scoreboardManager.openScoreboard(new DefaultScoreboard(), p);
        final Component header = Component.text("ContinentalMC", NamedTextColor.BLUE);
        final Component footer = Component.text("----------------");
        p.sendPlayerListHeaderAndFooter(header, footer);
        ContinentalManagers.world(instance).votingManager().getVoteBar().addPlayer(p);
        if (ContinentalManagers.world(instance).votingManager() != null && ContinentalManagers.world(instance).dataStorer().votingOption != null) {
            InventoryEnum inventoryEnum = ContinentalManagers.world(instance).dataStorer().votingOption.getDefaultInventory();
            if (inventoryEnum != null)
                ContinentalManagers.inventoryManager.assignInventory(p, inventoryEnum);
            if (ContinentalManagers.yearManager.getYearBar(instance) != null) {
                ContinentalManagers.yearManager.getYearBar(instance).addPlayer(p);
            } else {
                ContinentalManagers.yearManager.addBar(instance);
                ContinentalManagers.yearManager.getYearBar(instance).addPlayer(p);
            }
        }
        ContinentalManagers.world(instance).clientEntsToLoad().loadPlayer(p);

    }

    @Override
    public void removePlayer(CPlayer p) {
        ContinentalManagers.world(getInstance()).votingManager().getVoteBar().removePlayer(p);
        if (p.getCountry() != null) {
            p.getCountry().removePlayer(p, true);
        }
    }

    @Override
    public void playerBlockInteract(PlayerBlockInteractEvent e) {
        if (ContinentalManagers.world(e.getInstance()).dataStorer().votingOption != null)
            ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);

    }

    @Override
    public void playerUseItem(PlayerUseItemEvent e) {
        if (ContinentalManagers.world(e.getInstance()).dataStorer().votingOption != null)
            ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);

    }

    @Override
    public void playerStartDigging(PlayerStartDiggingEvent e) {
        if (ContinentalManagers.world(e.getInstance()).dataStorer().votingOption != null)
            ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);

    }
}
