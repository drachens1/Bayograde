package org.drachens.temporary.worlds;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.World;
import org.drachens.player_types.CPlayer;
import org.drachens.temporary.scoreboards.DefaultScoreboard;

import java.time.LocalTime;
import java.util.List;

public class ContinentalWorld extends World {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;

    public ContinentalWorld() {
        super(MinecraftServer.getInstanceManager().createInstanceContainer(), new Pos(0, 1, 0));
        getInstance().setBlock(0, 0, 0, Block.LAPIS_BLOCK);
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
        }
        if (ContinentalManagers.yearManager.getYearBar(instance) == null) {
            ContinentalManagers.yearManager.addBar(instance);
        }
        ContinentalManagers.yearManager.getYearBar(instance).addPlayer(p);
        ContinentalManagers.world(instance).clientEntsToLoad().loadPlayer(p);
    }

    @Override
    public void removePlayer(CPlayer p) {
        ContinentalManagers.world(getInstance()).votingManager().getVoteBar().removePlayer(p);
        ContinentalManagers.yearManager.getYearBar(getInstance()).removePlayer(p);
        if (p.getCountry() != null) {
            p.getCountry().removePlayer(p);
        }
        p.addPlayTime(LocalTime.now());
        Country country = p.getCountry();
        if (country != null) country.removePlayer(p);
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
    public void playerMove(PlayerMoveEvent e) {
        if (ContinentalManagers.world(e.getInstance()).dataStorer().votingWinner == null) return;
        CPlayer p = (CPlayer) e.getPlayer();
        Point point = p.getTargetBlockPosition(10);
        if (point == null) return;
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(point);
        if (province == null) return;
        if (province.getOccupier() == null) {
            p.sendActionBar(Component.text("Unoccupied", NamedTextColor.GOLD, TextDecoration.BOLD));
        } else {
            if (province.getOccupier().getNameComponent() == null) {
                System.err.println("Something went wrong drastically");
                return;
            }
            if (ContinentalManagers.world(e.getInstance()).dataStorer().votingWinner == VotingWinner.ww2_troops) {
                if (province.getTroops() != null && province.getOccupier() != null && p.getCountry() != null && (province.getOccupier().isMilitaryAlly(p.getCountry())||p.getCountry()==province.getOccupier())) {
                    List<Troop> troops = province.getTroops();
                    float meanHp = 0f;
                    float meanOrg = 0f;
                    float meanStrength = 0f;
                    float meanDef = 0f;
                    float meanSped = 0f;
                    float meanDmg = 0f;
                    int troopCount = troops.size();
                    if (troopCount == 0) {
                        p.sendActionBar(province.getOccupier().getNameComponent());
                        return;
                    }
                    for (Troop troop : troops) {
                        meanHp += troop.getHealth();
                        meanDmg += troop.getDamage();
                        meanDef += troop.getDefence();
                        meanSped += troop.getSpeed();
                        meanOrg += troop.getOrg();
                        meanStrength += troop.getStrength();
                    }

                    p.sendActionBar(Component.text()
                            .append(Component.text("Divs: "))
                            .append(Component.text(troopCount))
                            .append(Component.text(" | HP: "))
                            .append(Component.text(meanHp))
                            .append(Component.text(" | DMG: "))
                            .append(Component.text(meanDmg))
                            .append(Component.text(" | DEF: "))
                            .append(Component.text(meanDef))
                            .append(Component.text(" | SPD: "))
                            .append(Component.text(meanSped))
                            .append(Component.text(" | ORG: "))
                            .append(Component.text(meanOrg))
                            .append(Component.text(" | STR: "))
                            .append(Component.text(meanStrength)));
                    return;
                }
            }
            p.sendActionBar(province.getOccupier().getNameComponent());
        }
    }
}
