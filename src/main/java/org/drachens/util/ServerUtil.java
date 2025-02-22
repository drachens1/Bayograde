package org.drachens.util;

import dev.ng5m.Constants;
import dev.ng5m.event.CancelPurchaseEvent;
import dev.ng5m.event.PurchaseEvent;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.Weather;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.WorldManager;
import org.drachens.Manager.defaults.CentralEventManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.per_instance.vote.VotingManager;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.cmd.CosmeticsCMD;
import org.drachens.cmd.Dev.DevCMD;
import org.drachens.cmd.Dev.OpMeCMD;
import org.drachens.cmd.Dev.WhoisCMD;
import org.drachens.cmd.Fly.FlyCMD;
import org.drachens.cmd.Fly.FlyspeedCMD;
import org.drachens.cmd.Msg.MsgCMD;
import org.drachens.cmd.Msg.ReplyCMD;
import org.drachens.cmd.SpawnCMD;
import org.drachens.cmd.StoreCMD;
import org.drachens.cmd.TeleportCMD;
import org.drachens.cmd.game.GameCMD;
import org.drachens.cmd.game.PlaytimeCMD;
import org.drachens.cmd.gamecreate.GameManageCMD;
import org.drachens.cmd.gamecreate.start.StartGameCMD;
import org.drachens.cmd.help.HelpCMD;
import org.drachens.cmd.settings.SettingsCMD;
import org.drachens.cmd.vote.VoteCMD;
import org.drachens.cmd.vote.VotingOptionCMD;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.additional.GlobalGameWorldClass;
import org.drachens.dataClasses.datastorage.DataStorer;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.events.NewDay;
import org.drachens.events.countries.CountryJoinEvent;
import org.drachens.events.ranks.RankAddEvent;
import org.drachens.events.ranks.RankRemoveEvent;
import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.drachens.fileManagement.customTypes.player.PlayerInfoEntry;
import org.drachens.generalGame.ContinentalWorld;
import org.drachens.generalGame.country.CountryCMD;
import org.drachens.generalGame.faction.FactionCMD;
import org.drachens.generalGame.scoreboards.DefaultCountryScoreboard;
import org.drachens.generalGame.view_modes.ViewModesCMD;
import org.drachens.player_types.CPlayer;
import org.drachens.store.other.Rank;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.drachens.Manager.defaults.ContinentalManagers.putWorldClass;
import static org.drachens.util.Messages.logCmd;
import static org.drachens.util.OtherUtil.runThread;

@Getter
public enum ServerUtil {
    ;
    @Getter
    private static final HashSet<Chunk> allowedChunks = new HashSet<>();
    private static final HashMap<PlayerConnection, List<Rank>> playerRanks = new HashMap<>();
    private static MinecraftServer srv;
    private static GlobalEventHandler globalEventHandler;

    public static void setup() {
        globalEventHandler = MinecraftServer.getGlobalEventHandler();
    }

    public static void initSrv() {
        if (null != ServerUtil.srv) {
            return;
        }
        srv = MinecraftServer.init();
    }

    public static void startSrv() {
        if (null == ServerUtil.srv || MinecraftServer.isStarted()) {
            return;
        }
        ServerPropertiesFile serverPropertiesFile = ContinentalManagers.configFileManager.getServerPropertiesFile();
        if (serverPropertiesFile.isVelocity()) {
            VelocityProxy.enable(serverPropertiesFile.getSecret());
        } else MojangAuth.init();
        srv.start(serverPropertiesFile.getHost(), serverPropertiesFile.getPort());
    }

    public static void setupAll(List<Command> cmd, ScoreboardManager scoreboardManager) {
        setup();
        ContinentalManagers.configFileManager.startup();

        WorldManager worldManager = ContinentalManagers.worldManager;
        ContinentalWorld continentalWorld = new ContinentalWorld();
        worldManager.registerWorld(continentalWorld);
        worldManager.setDefaultWorld(continentalWorld);

        List<VotingOption> votingOptions = new ArrayList<>();
        votingOptions.add(VotingWinner.ww2_troops.getVotingOption());
        votingOptions.add(VotingWinner.ww2_clicks.getVotingOption());

        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            instance.createInitializeWorldBorderPacket();
            instance.setWeather(Weather.CLEAR);
            instance.setTime(0);
            instance.setTimeRate(0);
            putWorldClass(instance, new GlobalGameWorldClass(
                            new CountryDataManager(instance, new ArrayList<>()),
                            new ClientEntsToLoad(),
                            new VotingManager(instance),
                            new ProvinceManager(),
                            new DataStorer()
                    )
            );
        }
        GlobalEventHandler globEHandler = ServerUtil.globalEventHandler;

        globEHandler.addListener(PlayerBlockBreakEvent.class, e -> e.setCancelled(false));

        HashMap<UUID, PlayerInfoEntry> playerUUIDMap = new HashMap<>();

        globEHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            final Player p = e.getPlayer();
            runThread(()->playerUUIDMap.remove(p.getUuid()).setPlayer((CPlayer) p));
            e.setSpawningInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
            p.setRespawnPoint(new Pos(0, 1, 0));
        });

        globEHandler.addListener(RankAddEvent.class, e -> playerRanks.get(e.player().getPlayerConnection()).add(e.rank()));
        globEHandler.addListener(RankRemoveEvent.class, e -> playerRanks.get(e.player().getPlayerConnection()).remove(e.rank()));

        globEHandler.addListener(AsyncPlayerPreLoginEvent.class, e -> {
            GameProfile gameProfile = e.getGameProfile();
            Constants.BAN_MANAGER.isBanned(gameProfile.uuid());
            if (ContinentalManagers.configFileManager.getWhitelistFile().isActive() && !ContinentalManagers.configFileManager.getWhitelistFile().whiteListContains(gameProfile.uuid().toString())) {
                e.getConnection().kick(Component.text("You are not whitelisted"));
                System.out.println(gameProfile.name() + " tried to join the game but isn't whitelisted");
                return;
            }
            playerRanks.put(e.getConnection(), new ArrayList<>());
            playerUUIDMap.put(gameProfile.uuid(),new PlayerInfoEntry(gameProfile, ContinentalManagers.database.getTable("player_info")));
        });

        Function<PlayerChatEvent, Component> chatEvent = e -> {
            final CPlayer p = (CPlayer) e.getPlayer();
            List<Component> components = new ArrayList<>();
            Country c = p.getCountry();
            Component prefix;
            if (null == c) {
                prefix = Component.text("spectator", NamedTextColor.GRAY, TextDecoration.BOLD);
            } else {
                prefix = c.getPrefix();
            }
            Rank rank = p.getDominantRank();
            components.add(rank.prefix);
            components.add(Component.text(" "));
            components.add(prefix);
            components.add(Component.text(" "));
            components.add(p.getName().color(rank.color));
            components.add(Component.text(" : ", NamedTextColor.GRAY));
            components.add(Component.text(e.getRawMessage(), NamedTextColor.GRAY));
            components.add(Component.text(" "));
            if (p.isPremium()&&p.getPlayerJson().isSuffixActive()) {
                components.add(Component.text(p.getPlayerJson().getSuffix()));
            }
            return Component.text().append(components).build();
        };

        globEHandler.addListener(PlayerChatEvent.class, e -> e.setFormattedMessage(chatEvent.apply(e)));

        globEHandler.addListener(PlayerCommandEvent.class, e -> {
            final Player p = e.getPlayer();
            logCmd(p.getUsername(), e.getCommand(), p.getInstance());
        });

        GUIManager guiManager = ContinentalManagers.guiManager;

        globEHandler.addListener(InventoryPreClickEvent.class, guiManager::handleClick)
                .addListener(InventoryOpenEvent.class, guiManager::handleOpen)
                .addListener(InventoryCloseEvent.class, guiManager::handleClose);

        globEHandler.addListener(PlayerMoveEvent.class, e -> {
            final Player p = e.getPlayer();
            if (0 > p.getPosition().y()) {
                p.teleport(ContinentalManagers.worldManager.getWorld(p.getInstance()).getSpawnPoint());
            }
        });


        globEHandler.addListener(CountryJoinEvent.class, e -> e.p().setCountry(e.joined()));

        AtomicInteger i = new AtomicInteger();
        globEHandler.addListener(NewDay.class,e->{
            if (7 == i.get()){
                ContinentalManagers.world(e.world()).countryDataManager().getCountries().forEach(country -> country.nextWeek(e));
                i.set(0);
            }
            i.getAndIncrement();
        });


        globEHandler.addListener(NewDay.class, e -> {
            e.world().getPlayers().forEach(player -> {
                ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(player);
                if (continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard) {
                    defaultCountryScoreboard.updateAll();
                }
            });
            String time = e.day() + "/" + e.month() + '|' + e.year();
            ContinentalManagers.playerModsManager.getPlayers(e.world()).forEach(player -> player.sendPluginMessage("continentalmod:time", time));
            runThread(()->ContinentalManagers.world(e.world()).countryDataManager().getCountries().forEach(country -> country.nextDay(e)));
        });

        globEHandler.addListener(PlayerBlockBreakEvent.class, e -> e.setCancelled(true));

        List<VotingOptionCMD> votingOptionsCMD = new ArrayList<>();
        for (VotingOption votingOption : votingOptions) votingOptionsCMD.add(new VotingOptionCMD(votingOption));

        CommandManager commandManager = MinecraftServer.getCommandManager();

        //Register cmds
        commandManager.register(new HelpCMD());
        commandManager.register(new ReplyCMD());
        commandManager.register(new MsgCMD());
        commandManager.register(new FlyCMD());

        commandManager.register(new FlyspeedCMD());
        commandManager.register(new CountryCMD());
        commandManager.register(new TeleportCMD());
        commandManager.register(new VoteCMD(votingOptionsCMD));
        commandManager.register(new SpawnCMD());
        commandManager.register(new FactionCMD());
        commandManager.register(new StoreCMD());
        commandManager.register(new CosmeticsCMD());
        commandManager.register(new ViewModesCMD());
        commandManager.register(new DevCMD());

        commandManager.register(new PlaytimeCMD());

        commandManager.register(new WhoisCMD());

        commandManager.register(new OpMeCMD());

        commandManager.register(new SettingsCMD(votingOptionsCMD));
        commandManager.register(new StartGameCMD(votingOptionsCMD));
        commandManager.register(new GameManageCMD());
        commandManager.register(new GameCMD());

        for (Command command : cmd) {
            MinecraftServer.getCommandManager().register(command);
        }

        globEHandler.addListener(PlayerBlockPlaceEvent.class, e -> e.setCancelled(true));

        globEHandler.addListener(PurchaseEvent.class,e-> System.out.println(e.success() +" : "+ e.request()));
        globEHandler.addListener(CancelPurchaseEvent.class, e-> System.out.println(e.success() +" : "+ e.request()));

        new CentralEventManager();
        start();

    }

    public static void start() {
        startSrv();
    }


    public static Pos blockVecToPos(BlockVec blockVec) {
        return new Pos(blockVec.blockX(), blockVec.blockY(), blockVec.blockZ());
    }
}