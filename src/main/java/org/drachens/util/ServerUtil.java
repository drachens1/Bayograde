package org.drachens.util;

import dev.ng5m.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
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
import net.minestom.server.inventory.TransactionOption;
import net.minestom.server.item.ItemStack;
import net.minestom.server.scoreboard.Sidebar;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.*;
import org.drachens.cmd.Dev.Kill.killCMD;
import org.drachens.cmd.Dev.ListCMD;
import org.drachens.cmd.Dev.Permissions.PermissionsCMD;
import org.drachens.cmd.Dev.debug.CountryTypes.CountryHistoryCMD;
import org.drachens.cmd.Dev.debug.CountryTypes.CountryTypesCMD;
import org.drachens.cmd.Dev.debug.countryDebug.CountryDebugCMD;
import org.drachens.cmd.Dev.gamemode.GamemodeCMD;
import org.drachens.cmd.Dev.help.HelpCMD;
import org.drachens.cmd.Dev.operator;
import org.drachens.cmd.Dev.testCMD;
import org.drachens.cmd.Dev.whitelist.WhitelistCMD;
import org.drachens.cmd.Fly.FlyCMD;
import org.drachens.cmd.Fly.FlyspeedCMD;
import org.drachens.cmd.Msg.MsgCMD;
import org.drachens.cmd.Msg.ReplyCMD;
import org.drachens.cmd.TeleportCMD;
import org.drachens.cmd.ban.BanCMD;
import org.drachens.cmd.ban.UnbanCMD;
import org.drachens.cmd.country.CountryCMD;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.factory.FactoryType;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.Provinces.ProvinceManager;
import org.drachens.dataClasses.WorldClasses;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.events.Countries.CountryChangeEvent;
import org.drachens.events.Countries.CountryJoinEvent;
import org.drachens.events.Countries.CountryLeaveEvent;
import org.drachens.events.NewDay;
import org.drachens.events.RankAddEvent;
import org.drachens.events.RankRemoveEvent;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

import static org.drachens.Manager.AchievementsManager.addPlayerToAdv;
import static org.drachens.Manager.AchievementsManager.createAdvancements;
import static org.drachens.Manager.ConfigFileManager.*;
import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.Messages.globalBroadcast;
import static org.drachens.util.Messages.logCmd;
import static org.drachens.util.PermissionsUtil.playerOp;
import static org.drachens.util.PermissionsUtil.setupPerms;
import static org.drachens.util.PlayerUtil.addPlayerToCountryMap;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class ServerUtil {
    private static final List<Chunk> allowedChunks = new ArrayList<>();
    private static final HashMap<Instance, WorldClasses> worldClassesHashMap = new HashMap<>();
    private static final List<Player> cooldown = new ArrayList<>();
    private static MinecraftServer srv;
    private static GlobalEventHandler globalEventHandler;
    private static ProvinceManager provinceManager;

    public static void setup() {
        globalEventHandler = MinecraftServer.getGlobalEventHandler();
        provinceManager = new ProvinceManager();
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

    public static ProvinceManager getProvinceManager() {
        return provinceManager;
    }

    public static GlobalEventHandler getEventHandler() {
        System.out.println("Getting event handler");
        return globalEventHandler;
    }
    private static HashMap<Player, List<Rank>> playerRanks = new HashMap<>();

    public static void setupAll(List<Command> cmd, int countries, HashMap<CurrencyTypes, Currencies> defaultCurrencies, ItemStack[] defaultPlayerItems, List<IdeologyTypes> ideologyTypes, Ideology defaultIdeology, ScoreboardManager scoreboardManager) {
        initSrv();
        setup();
        //Create the instance(world)
        InstanceManager instMan = MinecraftServer.getInstanceManager();
        InstanceContainer instCon = instMan.createInstanceContainer();

        GlobalEventHandler globEHandler = getEventHandler();

        //Generate the world
        instCon.setGenerator(unit -> unit.modifier().fillHeight(0, -1, Block.LAPIS_BLOCK));

        //lighting
        instCon.setChunkSupplier(LightingChunk::new);

        globEHandler.addListener(PlayerBlockBreakEvent.class, e -> e.setCancelled(false));

        //VELOCITAY
        //VelocityProxy.enable("uZEnvlMqDPRr"); //TODO Change this to not be outed when adding it to the server
        MojangAuth.init();


        setupPerms();
        createAdvancements();
        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            instance.createInitializeWorldBorderPacket();
            instance.setWeather(Weather.CLEAR);
            instance.setTime(0);
            CountryDataManager countryDataManager = new CountryDataManager(instance, new ArrayList<>(),ideologyTypes,defaultIdeology);
            worldClassesHashMap.put(instance, new WorldClasses(
                            new YearManager(0, 10, (long) 1000.0, instance),
                            countryDataManager,
                            new MapGeneratorManager(instance, provinceManager, countryDataManager, countries, defaultCurrencies,defaultIdeology),
                            new ClientEntsToLoad()
                    )
            );
        }

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
            if (isBanned(p.getUuid())) {
                p.kick(getBanMSG(p));
                System.out.println(p.getUsername() + " tried to join the game but is banned");
                return;
            }
            if (getWhitelist().active() && !getWhitelist().getPlayers().contains(p.getUuid())) {
                p.kick("You are not whitelisted");
                System.out.println(p.getUsername() + " tried to join the game but isn't whitelisted");
                return;
            }
            loadPermissions(p);
            playerRanks.put(p,new ArrayList<>());
            getPlayersData(p.getUuid());
            addPlayerToCountryMap(p);
        });
        Function<Player, Component> displayNameSupplier = Player::getName;
        Rank r = new Rank(displayNameSupplier,compBuild("cool",NamedTextColor.BLUE),compBuild("cool2",NamedTextColor.BLUE),NamedTextColor.RED, "cool");

        globEHandler.addListener(PlayerSpawnEvent.class, e -> {
            Player p = e.getPlayer();
            p.setAllowFlying(true);
            globalBroadcast(p.getUsername() + " has joined the game");
            tabCreation(p);
            addPlayerToAdv(p);
            playerOp(p);
            p.getInventory().addItemStacks(Arrays.stream(defaultPlayerItems).toList(), TransactionOption.ALL);
            worldClassesHashMap.get(p.getInstance()).yearManager().addPlayer(p);

            worldClassesHashMap.get(p.getInstance()).clientEntsToLoad().loadPlayer(p);

            p.getInstance().enableAutoChunkLoad(false);
            r.addPlayer(p);
        });

        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            final Player p = e.getPlayer();
            Country country = getCountryFromPlayer(p);
            country.removePlayer(p, true);
            globalBroadcast(p.getUsername() + " has left the game");
            playerSave(p.getUuid());
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

        globEHandler.addListener(PlayerChatEvent.class, e -> {
           e.setChatFormat(chatEvent);
        });

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
            if (!allowedChunks.contains(p.getChunk())) {
                p.sendMessage(mergeComp(getPrefixes("system"), getCountryMessages("outOfBounds")));
                e.setCancelled(true);
            }
        });

        globEHandler.addListener(CountryJoinEvent.class, e -> addPlayerToCountryMap(e.getP(), e.getJoined()));
        globEHandler.addListener(CountryLeaveEvent.class, e -> addPlayerToCountryMap(e.getP(), null));
        globEHandler.addListener(CountryChangeEvent.class, e -> addPlayerToCountryMap(e.getP(), e.getJoined()));

        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> {
            Province p = provinceManager.getProvince(new Pos(e.getBlockPosition()));
            if (!e.getPlayer().isSneaking())return;
            if (playerHasCooldown(e.getPlayer())) return;
            cooldown(e.getPlayer());
            if (!p.isCapturable()) {
                return;
            }
            if (p.getOccupier() == null) {
                e.getPlayer().sendMessage(Component.text()
                        .append(Component.text("_________/", NamedTextColor.BLUE))
                        .append(Component.text("Neutral", NamedTextColor.GOLD))
                        .append(Component.text("\\_________\n", NamedTextColor.BLUE))
                        .append(Component.text("Leader: \n"))
                );
                return;
            }
            Country c = p.getOccupier();
            e.getPlayer().sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text(c.getName(), NamedTextColor.GOLD))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Leader: "))
                    .appendNewline()
                    .appendNewline()
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text("[JOIN]", NamedTextColor.GOLD))
                            .clickEvent(ClickEvent.runCommand("/country join " + c.getName()))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to join a country", NamedTextColor.GOLD)))
                    )
            );
        });
        final int[] week = {0};
        globEHandler.addListener(NewDay.class, e -> {
            CountryDataManager countryDataManager = getWorldClasses(e.getWorld()).countryDataManager();
            week[0]++;
            if (!(week[0] >= 7))for (Country country : countryDataManager.getCountries())country.calculateIncrease();
            week[0]=0;
            for (Player p : e.getWorld().getPlayers()){
                scoreboardManager.update(p);
            }
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
                sb.createLine(new Sidebar.ScoreboardLine(
                        "ideologies",
                        compBuild("Ideologies: ", NamedTextColor.BLUE),
                        i
                ));
                i++;
                for (Map.Entry<CurrencyTypes, Currencies> entry : country.getCurrenciesMap().entrySet()) {
                    ArrayList<Component> components = new ArrayList<>();
                    components.add(entry.getKey().getName());
                    components.add(compBuild(" : ", NamedTextColor.BLUE));
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
            }
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
        MinecraftServer.getCommandManager().register(new TeleportCMD());
        MinecraftServer.getCommandManager().register(new CountryTypesCMD());
        MinecraftServer.getCommandManager().register(new CountryHistoryCMD());
        MinecraftServer.getCommandManager().register(new CountryDebugCMD());

        for (Command command : cmd) {
            MinecraftServer.getCommandManager().register(command);
        }

        var schedular = MinecraftServer.getSchedulerManager();
        schedular.buildShutdownTask(() -> {
            System.out.println("Server shutting down");
            shutdown();
        });

        startup();
        new PermissionsManager();
        setupPrefixes();
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
}