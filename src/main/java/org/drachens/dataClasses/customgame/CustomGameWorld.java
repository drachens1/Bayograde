package org.drachens.dataClasses.customgame;

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
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.World;
import org.drachens.dataClasses.datastorage.DataStorer;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.events.system.StartGameEvent;
import org.drachens.generalGame.scoreboards.DefaultScoreboard;
import org.drachens.player_types.CPlayer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CustomGameWorld extends World {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    private final VotingOption votingOption;
    private final CPlayer p;
    private final List<CPlayer> players = new ArrayList<>();
    private final Component welcomeMessage;
    private boolean started = false;

    public CustomGameWorld(CPlayer p, VotingOption votingOption){
        super(MinecraftServer.getInstanceManager().createInstanceContainer(), new Pos(0, 1, 0));
        getInstance().setBlock(0,0,0,Block.DIAMOND_BLOCK);
        ContinentalManagers.worldManager.registerWorld(this);
        welcomeMessage=Component.text()
                .append(Component.text("| Welcome to a custom game.\n| These are basically the same as the global one but\n| the owner: "+p.getUsername()+" can activate certain DLC's \n| Also there is no voting period\n|\n| You are currently in the waiting period you can do anything but time wont advance\n| Until "+p.getUsername()+" starts the game", NamedTextColor.GREEN))
                .build();
        this.p=p;
        this.votingOption=votingOption;
        p.sendMessage(Component.text()
                        .append(Component.text("| Welcome to a custom game.\n| You can activate any DLC's if you have any\n| Commands:\n| /manage creation complete #Starts the game\n| /manage creation cancel #Cancels the waiting period\n| /manage invite <player> #Invites a player\n| /manage kick <player> #Kicks a player\n| /manage options #Shows the options/clickable options for commands to make it easier\n| You need to start the game for time to advance\n| That's all! have fun!",NamedTextColor.GREEN))
                .build());
        p.setLeaderOfOwnGame(true);
        ContinentalManagers.putWorldClass(getInstance(),new CustomGameWorldClass(new CountryDataManager(getInstance(), new ArrayList<>()),
                new ClientEntsToLoad(),
                new ProvinceManager(),
                new DataStorer()
                ));
        EventDispatcher.call(new StartGameEvent(getInstance(), votingOption));
        votingOption.getMapGenerator().generate(getInstance(),votingOption);
        p.setInstance(getInstance());
        ContinentalManagers.yearManager.getYearBar(getInstance()).cancelTask();
    }

    public void complete(){
        started=true;
        ContinentalManagers.yearManager.getYearBar(getInstance()).run(votingOption);
    }

    public void delete(){

    }

    @Override
    public void addPlayer(CPlayer p) {
        players.add(p);
        if (!p.isLeaderOfOwnGame())p.sendMessage(welcomeMessage);
        p.setInOwnGame(true);
        p.refreshCommands();
        Instance instance = p.getInstance();
        scoreboardManager.openScoreboard(new DefaultScoreboard(), p);
        final Component header = Component.text("ContinentalMC", NamedTextColor.BLUE);
        final Component footer = Component.text("----------------");
        p.sendPlayerListHeaderAndFooter(header, footer);
        InventoryEnum inventoryEnum = ContinentalManagers.world(instance).dataStorer().votingOption.getDefaultInventory();
        if (inventoryEnum != null)
            ContinentalManagers.inventoryManager.assignInventory(p, inventoryEnum);
        if (ContinentalManagers.yearManager.getYearBar(instance) == null) {
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

    public boolean hasStarted(){
        return started;
    }
}
