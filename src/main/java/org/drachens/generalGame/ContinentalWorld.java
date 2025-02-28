package org.drachens.generalGame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventDispatcher;
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
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.World;
import org.drachens.dataClasses.additional.GlobalGameWorldClass;
import org.drachens.events.VoteEvent;
import org.drachens.events.system.ResetEvent;
import org.drachens.generalGame.scoreboards.DefaultScoreboard;
import org.drachens.player_types.CPlayer;

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
        GlobalGameWorldClass globalGameWorldClass = ContinentalManagers.world(instance).getAsGlobalGameWorldClass();
        globalGameWorldClass.getAsGlobalGameWorldClass().votingManager().getVoteBar().addPlayer(p);
        if (null != globalGameWorldClass.getAsGlobalGameWorldClass().votingManager() && null != globalGameWorldClass.dataStorer().votingOption) {
            InventoryEnum inventoryEnum = globalGameWorldClass.dataStorer().votingOption.getDefaultInventory();
            if (null != inventoryEnum) ContinentalManagers.inventoryManager.assignInventory(p, inventoryEnum);
        }
        if (null == ContinentalManagers.yearManager.getYearBar(instance)) {
            ContinentalManagers.yearManager.addBar(instance);
        }
        ContinentalManagers.yearManager.getYearBar(instance).addPlayer(p);
        globalGameWorldClass.clientEntsToLoad().loadPlayer(p);
        if (globalGameWorldClass.votingManager().getVoteBar().isShown()) {
            if (p.isPremium() && p.getPlayerJson().isAutoVoteActive()) {
                EventDispatcher.call(new VoteEvent(p, VotingWinner.valueOf(p.getPlayerJson().getAutoVoteOption()).getVotingOption()));
            }
        }
    }

    @Override
    public void removePlayer(CPlayer p) {
        ContinentalManagers.world(getInstance()).getAsGlobalGameWorldClass().votingManager().getVoteBar().removePlayer(p);
        ContinentalManagers.yearManager.getYearBar(getInstance()).removePlayer(p);
        if (ContinentalManagers.world(getInstance()).isGlobalGameWorldClass()){
            GlobalGameWorldClass globalGameWorldClass = ContinentalManagers.world(getInstance()).getAsGlobalGameWorldClass();
            globalGameWorldClass.votingManager().removeVote(p);
        }
        if (null != p.getCountry()) {
            p.getCountry().removePlayer(p);
        }
        p.addPlayTime(LocalTime.now());
        Country country = p.getCountry();
        if (null != country) country.removePlayer(p);
        if (getInstance().getPlayers().isEmpty()){
            EventDispatcher.call(new ResetEvent(p.getInstance()));
        }
    }

    @Override
    public void playerBlockInteract(PlayerBlockInteractEvent e) {
        if (null != ContinentalManagers.world(e.getInstance()).dataStorer().votingOption)
            ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);
    }

    @Override
    public void playerUseItem(PlayerUseItemEvent e) {
        if (null != ContinentalManagers.world(e.getInstance()).dataStorer().votingOption)
            ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);
    }

    @Override
    public void playerStartDigging(PlayerStartDiggingEvent e) {
        if (null != ContinentalManagers.world(e.getInstance()).dataStorer().votingOption)
            ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);
    }

    @Override
    public void playerMove(PlayerMoveEvent e) {
        if (null == ContinentalManagers.world(e.getInstance()).dataStorer().votingWinner) return;
        CPlayer p = (CPlayer) e.getPlayer();
        Point point = p.getTargetBlockPosition(10);
        if (null == point) return;
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(point);
        if (null == province) return;
        if (null == province.getOccupier()) {
            p.sendActionBar(Component.text("Unoccupied", NamedTextColor.GOLD, TextDecoration.BOLD));
        } else {
            if (null == province.getOccupier().getComponentName()) {
                System.err.println("Something went wrong drastically");
                return;
            }
            if (VotingWinner.ww2_troops == ContinentalManagers.world(e.getInstance()).dataStorer().votingWinner) {
                if (null != province.getTroops() && null != province.getOccupier() && null != p.getCountry() && (province.getOccupier().isMilitaryAlly(p.getCountry()) || p.getCountry() == province.getOccupier())) {
                    List<Troop> troops = province.getTroops();
                    float meanHp = 0.0f;
                    float meanOrg = 0.0f;
                    float meanStrength = 0.0f;
                    float meanDef = 0.0f;
                    float meanSped = 0.0f;
                    float meanDmg = 0.0f;
                    int troopCount = troops.size();
                    if (0 == troopCount) {
                        p.sendActionBar(province.getOccupier().getComponentName());
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
            if (null == province.getOccupier()) return;
            p.sendActionBar(province.getOccupier().getComponentName());
        }
    }
}
