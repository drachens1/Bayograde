package org.drachens.dataClasses.customgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.World;
import org.drachens.dataClasses.datastorage.DataStorer;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.events.system.StartGameEvent;
import org.drachens.generalGame.scoreboards.DefaultScoreboard;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.Messages.broadcast;

public class CustomGameWorld extends World {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    private final VotingOption votingOption;
    private final List<CPlayer> players = new ArrayList<>();
    private final CPlayer opener;

    public CustomGameWorld(List<CPlayer> players, CPlayer opener, VotingOption votingOption){
        super(MinecraftServer.getInstanceManager().createInstanceContainer(), new Pos(0, 1, 0));
        ContinentalManagers.worldManager.registerWorld(this);
        this.opener=opener;
        this.votingOption=votingOption;
        ContinentalManagers.putWorldClass(getInstance(),new CustomGameWorldClass(
                new CountryDataManager(getInstance(), new ArrayList<>()),
                new ClientEntsToLoad(),
                new ProvinceManager(),
                new DataStorer()
                ));
        EventDispatcher.call(new StartGameEvent(getInstance(), votingOption));
        ContinentalManagers.yearManager.getYearBar(getInstance()).cancelTask();
        opener.setLeaderOfOwnGame(true);
        players.forEach(player -> player.setInstance(getInstance()));
        opener.sendMessage(Component.text()
                        .append(Component.text()
                                .append(Component.text("[COMPLETE] ",NamedTextColor.GOLD, TextDecoration.BOLD))
                                .clickEvent(ClickEvent.runCommand("/manage creation complete"))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to start the game",NamedTextColor.GREEN))))
                .append(Component.text()
                        .append(Component.text(" [CANCEL]",NamedTextColor.GOLD, TextDecoration.BOLD))
                        .clickEvent(ClickEvent.runCommand("/manage creation cancel"))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to cancel the game",NamedTextColor.GREEN))))
                .build());
    }

    public void complete(){
        players.forEach(player -> player.setInIntermission(false));
        broadcast(Component.text()
                .append(MessageEnum.system.getComponent())
                .append(Component.text("Game has started!",NamedTextColor.GREEN))
                .build(),getInstance());
        ContinentalManagers.yearManager.getYearBar(getInstance()).run(votingOption);
    }

    public void delete(){
        opener.setLeaderOfOwnGame(false);
        players.forEach(player -> {
            player.setInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
            player.setInIntermission(false);
        });
    }

    @Override
    public void addPlayer(CPlayer p) {
        players.add(p);
        p.setInOwnGame(true);
        p.refreshCommands();
        Instance instance = p.getInstance();
        scoreboardManager.openScoreboard(new DefaultScoreboard(), p);
        InventoryEnum inventoryEnum = votingOption.getDefaultInventory();
        if (null != inventoryEnum) ContinentalManagers.inventoryManager.assignInventory(p, inventoryEnum);
        if (null == ContinentalManagers.yearManager.getYearBar(instance)) {
            ContinentalManagers.yearManager.addBar(instance);
        }
        ContinentalManagers.yearManager.getYearBar(instance).addPlayer(p);
        ContinentalManagers.world(instance).clientEntsToLoad().loadPlayer(p);
    }

    @Override
    public void removePlayer(CPlayer p) {
        players.remove(p);
        p.setLeaderOfOwnGame(false);
        p.refreshCommands();
        ContinentalManagers.yearManager.getYearBar(getInstance()).removePlayer(p);
        if (null != p.getCountry()) {
            p.getCountry().removePlayer(p);
        }
        p.addPlayTime(LocalTime.now());
        Country country = p.getCountry();
        if (null != country) country.removePlayer(p);
        if (getInstance().getPlayers().isEmpty()){
            ContinentalManagers.removeWorldClass(getInstance());
            ContinentalManagers.worldManager.registerWorld(this);
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
