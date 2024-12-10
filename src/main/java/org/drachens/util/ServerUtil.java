package org.drachens.util;

import dev.ng5m.CPlayer;
import dev.ng5m.Constants;
import dev.ng5m.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Main;
import org.drachens.Manager.PermissionsManager;
import org.drachens.Manager.WhitelistManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.MessageManager;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.VotingWinner;
import org.drachens.Manager.defaults.scheduler.ContinentalScheduler;
import org.drachens.Manager.defaults.scheduler.ContinentalSchedulerManager;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.per_instance.vote.VotingManager;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.cmd.*;
import org.drachens.cmd.Dev.*;
import org.drachens.cmd.Dev.Kill.killCMD;
import org.drachens.cmd.Dev.Permissions.PermissionsCMD;
import org.drachens.cmd.Dev.debug.debugCMD;
import org.drachens.cmd.Dev.gamemode.GamemodeCMD;
import org.drachens.cmd.Dev.help.HelpCMD;
import org.drachens.cmd.Dev.serverManagement.StopCMD;
import org.drachens.cmd.Dev.whitelist.WhitelistCMD;
import org.drachens.cmd.Fly.FlyCMD;
import org.drachens.cmd.Fly.FlyspeedCMD;
import org.drachens.cmd.Msg.MsgCMD;
import org.drachens.cmd.Msg.ReplyCMD;
import org.drachens.cmd.ban.BanCMD;
import org.drachens.cmd.ban.UnbanCMD;
import org.drachens.cmd.example.ExampleCMD;
import org.drachens.cmd.plan.PlanCMD;
import org.drachens.cmd.vote.VoteCMD;
import org.drachens.cmd.vote.VotingOptionCMD;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.DataStorer;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.WorldClasses;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.events.Countries.CountryChangeEvent;
import org.drachens.events.Countries.CountryJoinEvent;
import org.drachens.events.NewDay;
import org.drachens.events.RankAddEvent;
import org.drachens.events.RankRemoveEvent;
import org.drachens.events.System.ResetEvent;
import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.drachens.temporary.country.CountryCMD;
import org.drachens.temporary.country.diplomacy.demand.DemandCMD;
import org.drachens.temporary.faction.FactionCMD;
import org.drachens.temporary.scoreboards.DefaultScoreboard;
import org.drachens.temporary.scoreboards.country.DefaultCountryScoreboard;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.drachens.util.KyoriUtil.getPrefixes;
import static org.drachens.util.Messages.globalBroadcast;
import static org.drachens.util.Messages.logCmd;

public class ServerUtil {
    private static final List<Chunk> allowedChunks = new ArrayList<>();
    private static final HashMap<Instance, WorldClasses> worldClassesHashMap = new HashMap<>();
    private static final HashMap<Player, List<Rank>> playerRanks = new HashMap<>();
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

        Main.initHooks();
    }

    public static void startSrv() {
        if (srv == null || MinecraftServer.isStarted()) {
            return;
        }
        ServerPropertiesFile serverPropertiesFile = ContinentalManagers.configFileManager.getServerPropertiesFile();
        if (serverPropertiesFile.isVelocity()) {
            VelocityProxy.enable(serverPropertiesFile.getSecret());
        } else
            MojangAuth.init();
        srv.start(serverPropertiesFile.getHost(), serverPropertiesFile.getPort());
    }

    public static GlobalEventHandler getEventHandler() {
        return globalEventHandler;
    }

    public static void stopSrv() {
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) p.kick("Server closed");
        MinecraftServer.stopCleanly();
    }

    public static void setupAll(List<Command> cmd, ScoreboardManager scoreboardManager) {
        setup();
        ContinentalManagers.configFileManager.startup();

        //Create the instance(world)
        InstanceManager instMan = MinecraftServer.getInstanceManager();
        InstanceContainer instCon = instMan.createInstanceContainer();

        //Generate the world
        instCon.setGenerator(unit -> unit.modifier().fillHeight(-1, 0, Block.LAPIS_BLOCK));

        List<VotingOption> votingOptions = new ArrayList<>();
        for (Map.Entry<VotingWinner, VotingOption> entry : ContinentalManagers.defaultsStorer.voting.getVotingOptionHashMap().entrySet()) {
            votingOptions.add(entry.getValue());
        }

        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            instance.createInitializeWorldBorderPacket();
            instance.setWeather(Weather.CLEAR);
            instance.setTime(0);
            instance.setTimeRate(0);
            worldClassesHashMap.put(instance, new WorldClasses(
                            new CountryDataManager(instance, new ArrayList<>()),
                            new ClientEntsToLoad(),
                            new VotingManager(votingOptions, instance),
                            new ProvinceManager(),
                            new DataStorer()
                    )
            );
        }
        new MessageManager();
        //lighting
        instCon.setChunkSupplier(LightingChunk::new);

        GlobalEventHandler globEHandler = getEventHandler();

        //VELOCITAY

        globEHandler.addListener(PlayerBlockBreakEvent.class, e -> e.setCancelled(false));

        globEHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            //Gets the player
            final Player p = e.getPlayer();
            e.setSpawningInstance(instCon);
            p.setRespawnPoint(new Pos(0, 1, 0));
        });

        globEHandler.addListener(RankAddEvent.class, e -> playerRanks.get(e.getPlayer()).add(e.getRank()));
        globEHandler.addListener(RankRemoveEvent.class, e -> playerRanks.get(e.getPlayer()).remove(e.getRank()));

        globEHandler.addListener(AsyncPlayerPreLoginEvent.class, e -> {
            final Player p = e.getPlayer();
            Constants.BAN_MANAGER.isBanned(p.getUuid());
            if (ContinentalManagers.configFileManager.getWhitelist().active() && !ContinentalManagers.configFileManager.getWhitelist().getPlayers().contains(p.getUuid())) {
                p.kick("You are not whitelisted");
                System.out.println(p.getUsername() + " tried to join the game but isn't whitelisted");
                return;
            }
            playerRanks.put(p, new ArrayList<>());
        });

        Function<Player, Component> displayNameSupplier = Player::getName;
        Rank r = new Rank(displayNameSupplier, Component.text("cool", NamedTextColor.BLUE), Component.text("cool2", NamedTextColor.BLUE), NamedTextColor.RED, "cool");

        globEHandler.addListener(PlayerSpawnEvent.class, e -> {
            Player p = e.getPlayer();
            p.setAllowFlying(true);
            scoreboardManager.openScoreboard(new DefaultScoreboard(),p);
            tabCreation(p);
            ContinentalManagers.permissions.playerOp(p);
            ContinentalManagers.world(p.getInstance()).votingManager().getVoteBar().addPlayer(p);
            ContinentalManagers.inventoryManager.assignInventory(p, InventoryEnum.defaultInv);
            if (ContinentalManagers.yearManager.getYearBar(p.getInstance()) != null) {
                ContinentalManagers.yearManager.getYearBar(p.getInstance()).addPlayer(p);
            } else {
                ContinentalManagers.yearManager.addBar(p.getInstance());
                ContinentalManagers.yearManager.getYearBar(p.getInstance()).addPlayer(p);
            }
            ContinentalManagers.configFileManager.createPlayersData(p);
            worldClassesHashMap.get(p.getInstance()).clientEntsToLoad().loadPlayer((CPlayer) p);

            p.getInstance().enableAutoChunkLoad(false);
            r.addPlayer(p);
            p.refreshCommands();
            ContinentalManagers.advancementManager.addPlayer((CPlayer) p);
        });

        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            final CPlayer p = (CPlayer) e.getPlayer();
            Country country = p.getCountry();
            if (country != null) country.removePlayer(p, true);
            globalBroadcast(p.getUsername() + " has left the game");
            p.getPlayerDataFile().save();
            p.addPlayTime(LocalTime.now());
        });

        Function<PlayerChatEvent, Component> chatEvent = e -> {
            final CPlayer p = (CPlayer) e.getPlayer();
            List<Component> components = new ArrayList<>();
            Country c = p.getCountry();
            Component prefix;
            if (c == null) {
                prefix = Component.text("spectator", NamedTextColor.GRAY, TextDecoration.BOLD);
            } else {
                prefix = c.getPrefix();
            }
            if (playerRanks.get(p) != null) {
                Rank rank = playerRanks.get(p).getFirst();
                components.add(rank.prefix);
                components.add(Component.text(" "));
                components.add(prefix);
                components.add(Component.text(" "));
                components.add(p.getName().color(rank.color));
                components.add(Component.text(" : ", NamedTextColor.GRAY));
                components.add(Component.text(e.getMessage(), NamedTextColor.GRAY));
                components.add(Component.text(" "));
                components.add(rank.suffix);
            } else {
                components.add(prefix);
                components.add(Component.text(" "));
                components.add(p.getName());
                components.add(Component.text(" : ", NamedTextColor.GRAY));
                components.add(Component.text(e.getMessage(), NamedTextColor.GRAY));
            }
            return Component.text().append(components).build();
        };

        globEHandler.addListener(PlayerChatEvent.class, e -> e.setChatFormat(chatEvent));

        globEHandler.addListener(PlayerCommandEvent.class, e -> {
            final Player p = e.getPlayer();
            logCmd(p.getUsername(), e.getCommand(), p.getInstance());
        });

        GUIManager guiManager = ContinentalManagers.guiManager;

        globEHandler.addListener(InventoryPreClickEvent.class, guiManager::handleClick)
                .addListener(InventoryOpenEvent.class, guiManager::handleOpen)
                .addListener(InventoryCloseEvent.class, guiManager::handleClose);

        WhitelistManager whitelistManager = new WhitelistManager();

        Component oob = Component.text()
                .append(getPrefixes("system"))
                .append(Component.text("You have went out of bounds! ", NamedTextColor.RED))
                .append(Component.text()
                        .append(Component.text("Click here", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .clickEvent(ClickEvent.runCommand("/tp 0 0"))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to teleport to spawn", NamedTextColor.GOLD)))
                )
                .append(Component.text(" To teleport to spawn", NamedTextColor.RED))
                .build();

        globEHandler.addListener(PlayerMoveEvent.class, e -> {
            final Player p = e.getPlayer();
            if (!allowedChunks.contains(p.getChunk()) && !worldClassesHashMap.get(p.getInstance()).votingManager().getVoteBar().isShown() && ContinentalManagers.world(p.getInstance()).dataStorer().votingOption!=null) {
                p.sendMessage(oob);
                e.setCancelled(true);
            }
        });


        globEHandler.addListener(CountryJoinEvent.class, e -> e.getP().setCountry(e.getJoined()));
        globEHandler.addListener(CountryChangeEvent.class, e -> e.getPlayer().setCountry(e.getJoined()));

        ContinentalSchedulerManager schedulerManager = ContinentalManagers.schedulerManager;
        schedulerManager.register(new ContinentalScheduler.Create(NewDay.class, e -> {
            if (!(e instanceof NewDay newDay)) return;
            ContinentalManagers.world(newDay.getInstance()).countryDataManager().getCountries().forEach(country -> country.nextWeek(newDay));
        }).setDelay(2).repeat().schedule());

        schedulerManager.register(new ContinentalScheduler.Create(NewDay.class, e->{
            if (!(e instanceof NewDay newDay)) return;
            newDay.getInstance().getPlayers().forEach(player -> {
                ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(player);
                if (continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard){
                    defaultCountryScoreboard.updateAll();
                }
            });
        }).setDelay(1).repeat().schedule());

        List<VotingOptionCMD> votingOptionsCMD = new ArrayList<>();
        for (VotingOption votingOption : votingOptions)
            votingOptionsCMD.add(new VotingOptionCMD(votingOption));


        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> {
            if (ContinentalManagers.world(e.getInstance()).dataStorer().votingOption != null)
                ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);
        });
        globEHandler.addListener(PlayerUseItemEvent.class, e -> {
            if (ContinentalManagers.world(e.getInstance()).dataStorer().votingOption != null)
                ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);
        });
        globEHandler.addListener(PlayerStartDiggingEvent.class, e -> {
            if (ContinentalManagers.world(e.getInstance()).dataStorer().votingOption != null)
                ContinentalManagers.world(e.getInstance()).dataStorer().votingOption.getWar().onClick(e);
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
        commandManager.register(new StopCMD());
        commandManager.register(new debugCMD());
        commandManager.register(new FactionCMD());
        commandManager.register(new SummonCMD());
        commandManager.register(new PlanCMD());
        commandManager.register(new StoreCMD());
        commandManager.register(new CosmeticsCMD());
        commandManager.register(new GoldCMD());
        commandManager.register(new PlaytimeCMD());
        commandManager.register(new DemandCMD());
        commandManager.register(new TechCMD());
        commandManager.register(new ExampleCMD());

        for (Command command : cmd) {
            MinecraftServer.getCommandManager().register(command);
        }

        new PermissionsManager();

        globEHandler.addListener(ResetEvent.class, e -> {
            CountryDataManager countryDataManager = worldClassesHashMap.get(e.getInstance()).countryDataManager();
            countryDataManager.getCountries().forEach((Country::endGame));
            ClientEntsToLoad clientEntsToLoad = worldClassesHashMap.get(e.getInstance()).clientEntsToLoad();
            new ArrayList<>(clientEntsToLoad.getClientSides(e.getInstance())).forEach((Clientside::dispose));
            clientEntsToLoad.reset();
            Instance instance = e.getInstance();
            CountryDataManager c = new CountryDataManager(instance, new ArrayList<>());
            worldClassesHashMap.put(instance, new WorldClasses(
                    c,
                    clientEntsToLoad,
                    worldClassesHashMap.get(instance).votingManager(),
                    worldClassesHashMap.get(instance).provinceManager(),
                    new DataStorer()
            ));
        });
        start();
    }

    public static void addChunk(Chunk chunk) {
        allowedChunks.add(chunk);
    }

    public static List<Chunk> getAllowedChunks() {
        return allowedChunks;
    }

    public static void start() {
        startSrv();
    }

    private static void tabCreation(Player p) {
        final Component header = Component.text("ContinentalMC", NamedTextColor.BLUE);
        final Component footer = Component.text("----------------");
        p.sendPlayerListHeaderAndFooter(header, footer);
    }

    public static WorldClasses getWorldClasses(Instance instance) {
        return worldClassesHashMap.get(instance);
    }

    public static Pos blockVecToPos(BlockVec blockVec) {
        return new Pos(blockVec.blockX(), blockVec.blockY(), blockVec.blockZ());
    }
}