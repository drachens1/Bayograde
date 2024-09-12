package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionCallback;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.scoreboard.Team;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.*;
import org.drachens.cmd.Dev.Kill.killCMD;
import org.drachens.cmd.Dev.ListCMD;
import org.drachens.cmd.Dev.Permissions.PermissionsCMD;
import org.drachens.cmd.Dev.gamemode.GamemodeCMD;
import org.drachens.cmd.Dev.help.HelpCMD;
import org.drachens.cmd.Dev.operator;
import org.drachens.cmd.Dev.testCMD;
import org.drachens.cmd.Dev.whitelist.WhitelistCMD;
import org.drachens.cmd.Fly.FlyCMD;
import org.drachens.cmd.Fly.FlyspeedCMD;
import org.drachens.cmd.Msg.MsgCMD;
import org.drachens.cmd.Msg.ReplyCMD;
import org.drachens.cmd.ban.BanCMD;
import org.drachens.cmd.ban.UnbanCMD;
import org.drachens.cmd.country.CountryCMD;
import org.drachens.cmd.country.Info;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.Provinces.ProvinceManager;
import org.drachens.dataClasses.WorldClasses;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.drachens.Manager.AchievementsManager.addPlayerToAdv;
import static org.drachens.Manager.AchievementsManager.createAdvancements;
import static org.drachens.Manager.ConfigFileManager.*;
import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.Messages.*;
import static org.drachens.util.PermissionsUtil.setupPerms;
import static org.drachens.util.PlayerUtil.addPlayerToCountryMap;

public class ServerUtil {
    private static MinecraftServer srv;
    private static GlobalEventHandler globalEventHandler;
    private static ProvinceManager provinceManager;
    public static void setup(){
        globalEventHandler = MinecraftServer.getGlobalEventHandler();
        provinceManager = new ProvinceManager();
    }
    public static void initSrv(){
        if (srv!=null){
            return;
        }
        srv = MinecraftServer.init();
    }
    public static void startSrv(){
        System.out.println("Start ");
        if (srv == null || MinecraftServer.isStarted()){
            System.out.println("start 2");
            return;
        }
        System.out.println("Start 3");
        srv.start("0.0.0.0",25565);
    }
    public static ProvinceManager getProvinceManager(){
        return provinceManager;
    }

    public static GlobalEventHandler getEventHandler(){
        System.out.println("Getting event handler");
        return globalEventHandler;
    }
    private static final HashMap<Instance, WorldClasses> worldClassesHashMap = new HashMap<>();
    public static void setupAll(List<Command> cmd){
        initSrv();
        setup();
        //Create the instance(world)
        InstanceManager instMan = MinecraftServer.getInstanceManager();
        InstanceContainer instCon = instMan.createInstanceContainer();

        GlobalEventHandler globEHandler = getEventHandler();

        //Generate the world
        instCon.setGenerator(unit -> unit.modifier().fillHeight(0,-1, Block.LAPIS_BLOCK));

        //lighting
        instCon.setChunkSupplier(LightingChunk::new);

        globEHandler.addListener(PlayerBlockBreakEvent.class, e -> e.setCancelled(false));

        //Mojang auth so online mode
        MojangAuth.init();

        setupPerms();
        createAdvancements();

        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()){
            instance.createInitializeWorldBorderPacket();
            instance.setWeather(Weather.CLEAR);
            instance.setTime(0);
            CountryDataManager countryDataManager = new CountryDataManager(instance,new ArrayList<>());
            worldClassesHashMap.put(instance,new WorldClasses(
                    new YearManager(0,10,(long) 10.0,instance),
                    countryDataManager,
                    new MapGeneratorManager(instance,provinceManager, countryDataManager)));
        }

        globEHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            //Gets the player
            final Player p = e.getPlayer();
            e.setSpawningInstance(instCon);
            p.setRespawnPoint(new Pos(0,1,0));
        });

        globEHandler.addListener(AsyncPlayerPreLoginEvent.class, e -> {
            final Player p = e.getPlayer();
            if (isBanned(p.getUuid())){
                p.kick(getBanMSG(p));
                System.out.println(p.getUsername()+" tried to join the game but is banned");
                return;
            }
            if (getWhitelist().active() && !getWhitelist().getPlayers().contains(p.getUuid())){
                p.kick("You are not whitelisted");
                System.out.println(p.getUsername()+" tried to join the game but isn't whitelisted");
                return;
            }
            loadPermissions(p);
            getPlayersData(p.getUuid());
            addPlayerToCountryMap(p);
        });

        globEHandler.addListener(PlayerSpawnEvent.class, e->{
            Player p = e.getPlayer();
            p.setAllowFlying(true);
            globalBroadcast(p.getUsername()+" has joined the game");
            tabCreation(p);
            addPlayerToAdv(p);

            worldClassesHashMap.get(p.getInstance()).getYearManager().addPlayer(p);

            Team spectator = MinecraftServer.getTeamManager().getTeam("spectator");
            spectator.addMember(p.getUsername());
            p.setTeam(spectator);
            p.getInstance().enableAutoChunkLoad(false);
        });

        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            final Player p = e.getPlayer();
            globalBroadcast(p.getUsername()+" has left the game");
            playerSave(p.getUuid());
        });

        globEHandler.addListener(PlayerChatEvent.class, e->{
            final Player p = e.getPlayer();
            String message = e.getMessage();
            Component prefix = compBuild("bug",NamedTextColor.GRAY);
            if(p.getTeam()!=null){
                prefix = p.getTeam().getPrefix();
            }
            final Component a = prefix;
            e.setChatFormat((sender) -> mergeComp(a,compBuild(p.getUsername() + ": " + message,NamedTextColor.GRAY)));
        });

        globEHandler.addListener(PlayerCommandEvent.class,e->{
            final Player p = e.getPlayer();
            logCmd(p.getUsername(),e.getCommand(),p.getInstance());
        });

        GUIManager guiManager = new GUIManager();
        EventNode<Event> inventoryListener = EventNode.all("all");

        inventoryListener.addListener(InventoryClickEvent.class, guiManager::handleClick)
                .addListener(InventoryOpenEvent.class, guiManager::handleOpen)
                .addListener(InventoryCloseEvent.class, guiManager::handleClose);

        globEHandler.addChild(inventoryListener);
        WhitelistManager whitelistManager = new WhitelistManager();


        globEHandler.addListener(PlayerBlockInteractEvent.class,e->{
            Province p = provinceManager.getProvince(new Pos(e.getBlockPosition()));
            if (cooldown(e.getPlayer()))return;
            cooldown(e.getPlayer());
            if (!p.isCapturable()){
                return;
            }
            if (p.getOccupier()==null){
                e.getPlayer().sendMessage(Component.text()
                        .append(Component.text("_________/",NamedTextColor.BLUE))
                        .append(Component.text("NEUTRAL",NamedTextColor.GOLD))
                        .append(Component.text("\\_________\n",NamedTextColor.BLUE))
                        .append(Component.text("Leader: \n"))
                );
                return;
            }
            Country c = p.getOccupier();
            e.getPlayer().sendMessage(Component.text()
                    .append(Component.text("_______/",NamedTextColor.BLUE))
                    .append(Component.text(c.getName(),NamedTextColor.GOLD))
                    .append(Component.text("\\_______",NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Leader: "))
                    .appendNewline()
                    .appendNewline()
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text("[JOIN]",NamedTextColor.GOLD))
                            .clickEvent(ClickEvent.runCommand("country join "+c.getName()))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,Component.text("Click to join a country",NamedTextColor.GOLD)))
                    )
            );
        });

        //Register cmds
        MinecraftServer.getCommandManager().register(new HelpCMD());
        MinecraftServer.getCommandManager().register(new operator());
        MinecraftServer.getCommandManager().register(new BanCMD());
        MinecraftServer.getCommandManager().register(new UnbanCMD());
        MinecraftServer.getCommandManager().register(new ListCMD());
        MinecraftServer.getCommandManager().register(new WhitelistCMD(whitelistManager));
        MinecraftServer.getCommandManager().register(new testCMD(guiManager));
        MinecraftServer.getCommandManager().register(new killCMD());
        MinecraftServer.getCommandManager().register(new PermissionsCMD());
        MinecraftServer.getCommandManager().register(new ReplyCMD());
        MinecraftServer.getCommandManager().register(new MsgCMD());
        MinecraftServer.getCommandManager().register(new GamemodeCMD());
        MinecraftServer.getCommandManager().register(new FlyCMD());
        MinecraftServer.getCommandManager().register(new FlyspeedCMD());
        MinecraftServer.getCommandManager().register(new CountryCMD());


        for (Command command : cmd){
            MinecraftServer.getCommandManager().register(command);
        }

        var schedular = MinecraftServer.getSchedulerManager();
        schedular.buildShutdownTask(() -> {
            System.out.println("Server shutting down");
            instCon.saveChunksToStorage();
            instMan.getInstances().forEach(Instance::saveChunksToStorage);
            shutdown();
        });

        startup();
        new PermissionsManager();

    }
    public static void start(){
        System.out.println("Server starting...");
        startSrv();
    }
    private static void tabCreation(Player p){
        final Component header = Component.text("ContinentalMC", NamedTextColor.BLUE);
        final Component footer = Component.text("----------------\n-----");
        p.sendPlayerListHeaderAndFooter(header,footer);
    }
    private static final List<Player> cooldown = new ArrayList<>();
    public static boolean cooldown(Player p){
        if (cooldown.contains(p))return true;
        cooldown.add(p);
        MinecraftServer.getSchedulerManager().buildTask(()-> cooldown.remove(p)).delay(100, ChronoUnit.MILLIS).schedule();
        return false;
    }
    public static WorldClasses getWorldClasses(Instance instance){
        return worldClassesHashMap.get(instance);
    }
    public static Suggestion getCountriesAutoComplete(Suggestion suggestion,Instance instance){
        for (String name : getWorldClasses(instance).getCountryDataManager().getNamesList()){
            suggestion.addEntry(new SuggestionEntry(name));
        }
        return suggestion;
    }
    public static List<String> getCountryNames(Instance instance){
        System.out.println("4");
        return getWorldClasses(instance).getCountryDataManager().getNamesList();
    }
    public static Suggestion suggestions(List<String> suggestion, Suggestion suggestions){
        System.out.println("Suggestions size"+suggestion.size());
        for (String s : suggestion) {
            s = s.substring(0,1).toUpperCase()+s.substring(1);
            suggestions.addEntry(new SuggestionEntry(s));
        }
        return suggestions;
    }
    public static Suggestion getSuggestionsBasedOnInput(Suggestion suggestionss, String input, Instance i) {
        List<String> suggestions = getCountryNames(i);
        input = input.trim();
        if (input.isEmpty())return suggestions(suggestions,suggestionss);
        System.out.println("suggestion input after and suggestions size:"+suggestions.size()+" Input length = "+input.length()+" input: "+input);
        String finalInput = input;
        return suggestions(suggestions.stream()
                .filter(suggestion -> suggestion.toLowerCase().startsWith(finalInput))
                .collect(Collectors.toList()),suggestionss);
    }
}
