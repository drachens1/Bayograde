package org.drachens.util;

import dev.ng5m.Constants;
import dev.ng5m.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.BlockVec;
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
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.scoreboard.Sidebar;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.PermissionsManager;
import org.drachens.Manager.WhitelistManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.MessageManager;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.per_instance.vote.VotingManager;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.cmd.Dev.Kill.killCMD;
import org.drachens.cmd.Dev.ListCMD;
import org.drachens.cmd.Dev.Permissions.PermissionsCMD;
import org.drachens.cmd.Dev.ResetCMD;
import org.drachens.cmd.Dev.debug.debugCMD;
import org.drachens.cmd.Dev.gamemode.GamemodeCMD;
import org.drachens.cmd.Dev.help.HelpCMD;
import org.drachens.cmd.Dev.operator;
import org.drachens.cmd.Dev.serverManagement.StopCMD;
import org.drachens.cmd.Dev.whitelist.WhitelistCMD;
import org.drachens.cmd.Fly.FlyCMD;
import org.drachens.cmd.Fly.FlyspeedCMD;
import org.drachens.cmd.Msg.MsgCMD;
import org.drachens.cmd.Msg.ReplyCMD;
import org.drachens.cmd.SpawnCMD;
import org.drachens.cmd.TeleportCMD;
import org.drachens.cmd.TestCMD;
import org.drachens.cmd.ban.BanCMD;
import org.drachens.cmd.ban.UnbanCMD;
import org.drachens.cmd.country.CountryCMD;
import org.drachens.cmd.vote.VoteCMD;
import org.drachens.cmd.vote.VotingOptionCMD;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.factory.FactoryType;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;
import org.drachens.dataClasses.WorldClasses;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.events.Countries.CountryChangeEvent;
import org.drachens.events.Countries.CountryJoinEvent;
import org.drachens.events.Countries.CountryLeaveEvent;
import org.drachens.events.NewDay;
import org.drachens.events.RankAddEvent;
import org.drachens.events.RankRemoveEvent;
import org.drachens.events.System.ResetEvent;
import org.drachens.events.System.StartGameEvent;
import org.drachens.interfaces.Voting.VotingOption;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.Messages.globalBroadcast;
import static org.drachens.util.Messages.logCmd;
import static org.drachens.util.PlayerUtil.addPlayerToCountryMap;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class ServerUtil {
    private static final List<Chunk> allowedChunks = new ArrayList<>();
    private static final HashMap<Instance, WorldClasses> worldClassesHashMap = new HashMap<>();
    private static final List<Player> cooldown = new ArrayList<>();
    private static MinecraftServer srv;
    private static GlobalEventHandler globalEventHandler;

    public static void setup() {
        globalEventHandler = MinecraftServer.getGlobalEventHandler();
    }

    public static void initSrv() {
        if (srv != null) {
            return;
        }
        srv = MinecraftServer.init();
    }

    public static void startSrv() {
        System.out.println("Start ");
        if (srv == null || MinecraftServer.isStarted()) {
            System.out.println("start 2");
            return;
        }
        System.out.println("Start 3");
        srv.start("0.0.0.0", 25565);
    }

    public static GlobalEventHandler getEventHandler() {
        System.out.println("Getting event handler");
        return globalEventHandler;
    }

    public static void stopSrv(){
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers())p.kick("Server closed");
        MinecraftServer.stopCleanly();
    }

    private static final HashMap<Player, List<Rank>> playerRanks = new HashMap<>();

    public static void setupAll(List<Command> cmd, ScoreboardManager scoreboardManager) {
        setup();
        setupPrefixes();

        //Create the instance(world)
        InstanceManager instMan = MinecraftServer.getInstanceManager();
        InstanceContainer instCon = instMan.createInstanceContainer();

        //Generate the world
        instCon.setGenerator(unit -> unit.modifier().fillHeight(-1,0,Block.LAPIS_BLOCK));

        List<VotingOption> votingOptions = new ArrayList<>();
        for (Map.Entry<String, VotingOption> entry : ContinentalManagers.defaultsStorer.voting.getVotingOptionHashMap().entrySet()){
            votingOptions.add(entry.getValue());
        }

        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            instance.createInitializeWorldBorderPacket();
            instance.setWeather(Weather.CLEAR);
            instance.setTime(0);
            worldClassesHashMap.put(instance, new WorldClasses(
                            new CountryDataManager(instance,new ArrayList<>()),
                            new ClientEntsToLoad(),
                            new VotingManager(votingOptions,instance),
                            new ProvinceManager()
                    )
            );
        }
        new MessageManager();
        //lighting
        instCon.setChunkSupplier(LightingChunk::new);

        GlobalEventHandler globEHandler = getEventHandler();

        //VELOCITAY
        //VelocityProxy.enable("uZEnvlMqDPRr"); //TODO Change this to not be outed when adding it to the server
        MojangAuth.init();

        globEHandler.addListener(PlayerBlockBreakEvent.class, e -> e.setCancelled(true));

        globEHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            //Gets the player
            final Player p = e.getPlayer();
            e.setSpawningInstance(instCon);
            p.setRespawnPoint(new Pos(0, 1, 0));
        });

        globEHandler.addListener(RankAddEvent.class,e-> playerRanks.get(e.getPlayer()).add(e.getRank()));
        globEHandler.addListener(RankRemoveEvent.class, e-> playerRanks.get(e.getPlayer()).remove(e.getRank()));

        globEHandler.addListener(AsyncPlayerPreLoginEvent.class, e -> {
            final Player p = e.getPlayer();
            Constants.BAN_MANAGER.isBanned(p.getUuid());
            if (ContinentalManagers.configFileManager.getWhitelist().active() && !ContinentalManagers.configFileManager.getWhitelist().getPlayers().contains(p.getUuid())) {
                p.kick("You are not whitelisted");
                System.out.println(p.getUsername() + " tried to join the game but isn't whitelisted");
                return;
            }
            ContinentalManagers.configFileManager.loadPermissions(p);
            playerRanks.put(p,new ArrayList<>());
            ContinentalManagers.configFileManager.getPlayersData(p.getUuid());
            addPlayerToCountryMap(p);
        });

        Function<Player, Component> displayNameSupplier = Player::getName;
        Rank r = new Rank(displayNameSupplier,compBuild("cool",NamedTextColor.BLUE),compBuild("cool2",NamedTextColor.BLUE),NamedTextColor.RED, "cool");

        globEHandler.addListener(PlayerSpawnEvent.class, e -> {
            Player p = e.getPlayer();
            p.setAllowFlying(true);
            globalBroadcast(p.getUsername() + " has joined the game");
            scoreboardManager.getScoreboard("default").add(p);
            tabCreation(p);
            ContinentalManagers.achievementsManager.addPlayerToAdv(p);
            ContinentalManagers.permissions.playerOp(p);
            ContinentalManagers.world(p.getInstance()).votingManager().getVoteBar().addPlayer(p);
            ContinentalManagers.inventoryManager.assignInventory(p,"default");
            if(ContinentalManagers.yearManager.getYearBar(p.getInstance())!=null){
                ContinentalManagers.yearManager.getYearBar(p.getInstance()).addPlayer(p);
            }else {
                System.out.println("year bar was null");
                ContinentalManagers.yearManager.addBar(p.getInstance());
                ContinentalManagers.yearManager.getYearBar(p.getInstance()).addPlayer(p);
            }

            worldClassesHashMap.get(p.getInstance()).clientEntsToLoad().loadPlayer(p);

            p.getInstance().enableAutoChunkLoad(false);
            r.addPlayer(p);
        });

        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            final Player p = e.getPlayer();
            Country country = getCountryFromPlayer(p);
            if (country!=null) country.removePlayer(p, true);
            globalBroadcast(p.getUsername() + " has left the game");
            ContinentalManagers.configFileManager.playerSave(p.getUuid());
        });

        Function<PlayerChatEvent, Component> chatEvent = e -> {
            final Player p = e.getPlayer();
            List<Component> components = new ArrayList<>();
            Country c = getCountryFromPlayer(p);
            Component prefix;
            if (c==null){
                prefix = compBuild("spectator", NamedTextColor.GRAY,TextDecoration.BOLD);
            }else{
                prefix = c.getPrefix();
            }
            if (playerRanks.get(p)!=null){
                Rank rank = playerRanks.get(p).getFirst();
                components.add(rank.prefix);
                components.add(Component.text(" "));
                components.add(prefix);
                components.add(Component.text(" "));
                components.add(p.getName().color(rank.color));
                components.add(Component.text(" : ",NamedTextColor.GRAY));
                components.add(Component.text(e.getMessage(),NamedTextColor.GRAY));
                components.add(Component.text(" "));
                components.add(rank.suffix);
            }else {
                components.add(prefix);
                components.add(Component.text(" "));
                components.add(p.getName());
                components.add(Component.text(" : ",NamedTextColor.GRAY));
                components.add(Component.text(e.getMessage(),NamedTextColor.GRAY));
            }
            return mergeComp(components);
        };

        globEHandler.addListener(PlayerChatEvent.class, e -> e.setChatFormat(chatEvent));

        globEHandler.addListener(PlayerCommandEvent.class, e -> {
            final Player p = e.getPlayer();
            logCmd(p.getUsername(), e.getCommand(), p.getInstance());
        });

        GUIManager guiManager = new GUIManager();
        EventNode<Event> inventoryListener = EventNode.all("all");

        inventoryListener.addListener(InventoryClickEvent.class, guiManager::handleClick)
                .addListener(InventoryOpenEvent.class, guiManager::handleOpen)
                .addListener(InventoryCloseEvent.class, guiManager::handleClose);

        globEHandler.addChild(inventoryListener);
        WhitelistManager whitelistManager = new WhitelistManager();

        globEHandler.addListener(PlayerMoveEvent.class, e -> {
            final Player p = e.getPlayer();
            if (!allowedChunks.contains(p.getChunk()) && !worldClassesHashMap.get(e.getInstance()).votingManager().getVoteBar().isShown()) {
                p.sendMessage(mergeComp(getPrefixes("system"), getCountryMessages("outOfBounds")));
                e.setCancelled(true);
            }
        });

        globEHandler.addListener(CountryJoinEvent.class, e -> addPlayerToCountryMap(e.getP(), e.getJoined()));
        globEHandler.addListener(CountryLeaveEvent.class, e -> addPlayerToCountryMap(e.getP(), null));
        globEHandler.addListener(CountryChangeEvent.class, e -> addPlayerToCountryMap(e.getP(), e.getJoined()));

        globEHandler.addListener(NewDay.class, e -> {
            CountryDataManager countryDataManager = getWorldClasses(e.getWorld()).countryDataManager();

            for (Country country : countryDataManager.getCountries())country.calculateIncrease();
            for (Country country : countryDataManager.getCountries()) {
                Sidebar sb = new Sidebar(compBuild(country.getName(), NamedTextColor.GOLD, TextDecoration.BOLD));
                int i = 0;
                HashMap<FactoryType, Integer> num = new HashMap<>();
                for (PlaceableFactory placeableFactory : country.getPlaceableFactories()) {
                    if (num.containsKey(placeableFactory.getFactoryType())) {
                        int a = num.get(placeableFactory.getFactoryType());
                        a += placeableFactory.lvl();
                        num.put(placeableFactory.getFactoryType(), a);
                    } else {
                        num.put(placeableFactory.getFactoryType(), placeableFactory.lvl());
                    }
                }
                for (Map.Entry<FactoryType, Integer> entry : num.entrySet()) {
                    ArrayList<Component> components = new ArrayList<>();
                    components.add(entry.getKey().getName());
                    components.add(compBuild(" : ", NamedTextColor.BLUE));
                    components.add(compBuild(entry.getValue() + "", NamedTextColor.BLUE));
                    sb.createLine(new Sidebar.ScoreboardLine(
                            i + "",
                            mergeComp(components),
                            i
                    ));
                    i++;
                }
                for (Player p : country.getPlayer()) {
                    sb.addViewer(p);
                }
                for (Map.Entry<IdeologyTypes, Float> entry : country.getIdeology().getIdeologies().entrySet()){
                    if (entry.getValue()>1){
                        ArrayList<Component> components = new ArrayList<>();
                        components.add(entry.getKey().getName());
                        components.add(compBuild(" : ", NamedTextColor.BLUE));
                        components.add(compBuild(Math.round(entry.getValue()) + "%", NamedTextColor.BLUE));
                        sb.createLine(new Sidebar.ScoreboardLine(
                                i + "",
                                mergeComp(components),
                                i
                        ));
                        i++;
                    }
                }
                sb.createLine(new Sidebar.ScoreboardLine("total",compBuild("total"+country.getIdeology().total,NamedTextColor.GOLD),i));
                i++;
                sb.createLine(new Sidebar.ScoreboardLine(
                        "ideologies",
                        compBuild("Ideologies: ", NamedTextColor.BLUE),
                        i
                ));
                i++;
                for (Map.Entry<CurrencyTypes, Currencies> entry : country.getCurrenciesMap().entrySet()) {
                    ArrayList<Component> components = new ArrayList<>();
                    components.add(compBuild(entry.getValue().getAmount() + "", NamedTextColor.BLUE));
                    components.add(entry.getKey().getSymbol());
                    sb.createLine(new Sidebar.ScoreboardLine(
                            i + "",
                            mergeComp(components),
                            i
                    ));
                    i++;
                }

                sb.createLine(new Sidebar.ScoreboardLine(
                        "title",
                        compBuild("Currencies: ", NamedTextColor.BLUE),
                        i
                ));
                i++;
                for (Map.Entry<CurrencyTypes, Float> entry : country.getEconomyBoosts().entrySet()) {
                    ArrayList<Component> components = new ArrayList<>();
                    components.add(compBuild(entry.getValue() + "", NamedTextColor.BLUE));
                    components.add(entry.getKey().getSymbol());
                    sb.createLine(new Sidebar.ScoreboardLine(
                            i + "",
                            mergeComp(components),
                            i
                    ));
                    i++;
                }

                sb.createLine(new Sidebar.ScoreboardLine(
                        "tewjwr",
                        compBuild("Boosts: ", NamedTextColor.BLUE),
                        i
                ));
            }
        });

        List<VotingOptionCMD> votingOptionsCMD = new ArrayList<>();
        for (VotingOption votingOption : votingOptions)
            votingOptionsCMD.add(new VotingOptionCMD(votingOption));


        globEHandler.addListener(PlayerBlockInteractEvent.class, e->{
            if (ContinentalManagers.world(e.getInstance()).votingManager().getWinner()!=null)ContinentalManagers.world(e.getInstance()).votingManager().getWinner().getWar().onClick(e);
        });
        globEHandler.addListener(PlayerUseItemEvent.class, e->{
            if (ContinentalManagers.world(e.getInstance()).votingManager().getWinner()!=null)ContinentalManagers.world(e.getInstance()).votingManager().getWinner().getWar().onClick(e);
        });
        globEHandler.addListener(PlayerStartDiggingEvent.class, e->{
            if (ContinentalManagers.world(e.getInstance()).votingManager().getWinner()!=null) ContinentalManagers.world(e.getInstance()).votingManager().getWinner().getWar().onClick(e);
        });

        CommandManager commandManager = MinecraftServer.getCommandManager();

        //Register cmds
        commandManager.register(new HelpCMD());
        commandManager.register(new operator());
        commandManager.register(new BanCMD());
        commandManager.register(new UnbanCMD());
        commandManager.register(new ListCMD());
        commandManager.register(new WhitelistCMD(whitelistManager));
        commandManager.register(new killCMD());
        commandManager.register(new PermissionsCMD());
        commandManager.register(new ReplyCMD());
        commandManager.register(new MsgCMD());
        commandManager.register(new GamemodeCMD());
        commandManager.register(new FlyCMD());
        commandManager.register(new FlyspeedCMD());
        commandManager.register(new CountryCMD());
        commandManager.register(new TeleportCMD());
        commandManager.register(new VoteCMD(votingOptionsCMD));
        commandManager.register(new ResetCMD());
        commandManager.register(new SpawnCMD());
        commandManager.register(new TestCMD());
        commandManager.register(new StopCMD());
        commandManager.register(new debugCMD());

        for (Command command : cmd) {
            MinecraftServer.getCommandManager().register(command);
        }

        var schedular = MinecraftServer.getSchedulerManager();
        schedular.buildShutdownTask(() -> {
            System.out.println("Server shutting down");
            ContinentalManagers.configFileManager.shutdown();
        });

        ContinentalManagers.configFileManager.startup();
        new PermissionsManager();

        globEHandler.addListener(ResetEvent.class,e->{
            ClientEntsToLoad clientEntsToLoad = worldClassesHashMap.get(e.getInstance()).clientEntsToLoad();
            clientEntsToLoad.reset();
            for (Player p : e.getInstance().getPlayers()){
                clientEntsToLoad.unloadPlayer(p);
            }
            Instance instance = e.getInstance();
            CountryDataManager c = new CountryDataManager(instance, new ArrayList<>());
            worldClassesHashMap.put(instance, new WorldClasses(
                    c,
                    clientEntsToLoad,
                    worldClassesHashMap.get(instance).votingManager(),
                    worldClassesHashMap.get(instance).provinceManager()
            ));
        });

        globEHandler.addListener(StartGameEvent.class,e->{

        });

    }

    public static void addChunk(Chunk chunk) {
        allowedChunks.add(chunk);
    }

    public static List<Chunk> getAllowedChunks() {
        return allowedChunks;
    }

    public static void start() {
        System.out.println("Server starting...");
        startSrv();
    }

    private static void tabCreation(Player p) {
        final Component header = Component.text("ContinentalMC", NamedTextColor.BLUE);
        final Component footer = Component.text("----------------");
        p.sendPlayerListHeaderAndFooter(header, footer);
    }

    public static void cooldown(Player p) {
        if (cooldown.contains(p)) return;
        cooldown.add(p);
        MinecraftServer.getSchedulerManager().buildTask(() -> cooldown.remove(p)).delay(100, ChronoUnit.MILLIS).schedule();
    }

    public static boolean playerHasCooldown(Player p) {
        return cooldown.contains(p);
    }

    public static WorldClasses getWorldClasses(Instance instance) {
        return worldClassesHashMap.get(instance);
    }
    public static Pos blockVecToPos(BlockVec blockVec){
        return new Pos(blockVec.blockX(),blockVec.blockY(),blockVec.blockZ());
    }
}