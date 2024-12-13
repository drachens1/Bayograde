package org.drachens.temporary.worlds;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.World;
import org.drachens.temporary.scoreboards.DefaultScoreboard;

public class ContinentalWorld extends World {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    public ContinentalWorld() {
        super(MinecraftServer.getInstanceManager().createInstanceContainer());
        InstanceContainer instCon = getInstanceContainer();
        instCon.setGenerator(unit -> unit.modifier().fillHeight(-1, 0, Block.LAPIS_BLOCK));
    }

    @Override
    public void addPlayer(CPlayer p) {
        Instance instance = p.getInstance();
        scoreboardManager.openScoreboard(new DefaultScoreboard(),p);
        final Component header = Component.text("ContinentalMC", NamedTextColor.BLUE);
        final Component footer = Component.text("----------------");
        p.sendPlayerListHeaderAndFooter(header, footer);
        ContinentalManagers.permissions.playerOp(p);
        ContinentalManagers.world(instance).votingManager().getVoteBar().addPlayer(p);
        ContinentalManagers.inventoryManager.assignInventory(p, InventoryEnum.defaultInv);
        if (ContinentalManagers.yearManager.getYearBar(instance) != null) {
            ContinentalManagers.yearManager.getYearBar(instance).addPlayer(p);
        } else {
            ContinentalManagers.yearManager.addBar(instance);
            ContinentalManagers.yearManager.getYearBar(instance).addPlayer(p);
        }
        ContinentalManagers.configFileManager.createPlayersData(p);
        ContinentalManagers.world(instance).clientEntsToLoad().loadPlayer(p);

    }

    @Override
    public void removePlayer(CPlayer p) {

    }

    @Override
    public void playerMove(PlayerMoveEvent e) {

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

    @Override
    public void playerDisconnect(PlayerDisconnectEvent e) {

    }
}
