package org.drachens.util;

import net.minestom.server.MinecraftServer;
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
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.WhitelistManager;
import org.drachens.cmd.Dev.Kill.killCMD;
import org.drachens.cmd.Dev.ListCMD;
import org.drachens.cmd.ban.BanCMD;
import org.drachens.cmd.ban.UnbanCMD;
import org.drachens.cmd.Dev.help.HelpCMD;
import org.drachens.cmd.Dev.operator;
import org.drachens.cmd.Dev.testCMD;
import org.drachens.cmd.Dev.whitelist.WhitelistCMD;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.Provinces.ProvinceManager;

import static org.drachens.Manager.ConfigFileManager.*;
import static org.drachens.util.Messages.*;
import static org.drachens.util.PermissionsUtil.setupPerms;
import static org.drachens.util.WorldDataManagerUtil.setupWorldData;

public class ServerUtil {
    private static MinecraftServer srv;
    private static GlobalEventHandler globalEventHandler;
    public static String a;
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

    public static String getString(){
        return a;
    }

    public static void setupAll(){
        initSrv();
        setup();
        setupWorldData();
        ServerUtil.a = "b";
        //Create the instance(world)
        InstanceManager instMan = MinecraftServer.getInstanceManager();
        InstanceContainer instCon = instMan.createInstanceContainer();


        GlobalEventHandler globEHandler = getEventHandler();

        //Generate the world
        instCon.setGenerator(unit -> unit.modifier().fillHeight(0,40, Block.GRASS_BLOCK));

        //lighting
        instCon.setChunkSupplier(LightingChunk::new);

        globEHandler.addListener(PlayerBlockBreakEvent.class, e -> e.setCancelled(true));

        //Mojang auth so online mode
        MojangAuth.init();
        setupPerms();

        globEHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            //Gets the player
            final Player p = e.getPlayer();
            e.setSpawningInstance(instCon);
            p.setRespawnPoint(new Pos(0,49,0));
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
            globalBroadcast(p.getUsername()+" has joined the game");
            loadPermissions(p);
            getPlayersData(p.getUuid());
        });

        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            final Player p = e.getPlayer();
            globalBroadcast(p.getUsername()+" has left the game");
            playerSave(p.getUuid());
        });

        globEHandler.addListener(PlayerChatEvent.class, e->{
            final Player p = e.getPlayer();
            logMsg(p.getUsername(),e.getMessage(),p.getInstance());
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

        //Register cmds
        MinecraftServer.getCommandManager().register(new HelpCMD());
        MinecraftServer.getCommandManager().register(new operator());
        MinecraftServer.getCommandManager().register(new BanCMD());
        MinecraftServer.getCommandManager().register(new UnbanCMD());
        MinecraftServer.getCommandManager().register(new ListCMD());
        MinecraftServer.getCommandManager().register(new WhitelistCMD(whitelistManager));
        MinecraftServer.getCommandManager().register(new testCMD(guiManager));
        MinecraftServer.getCommandManager().register(new killCMD());


        var schedular = MinecraftServer.getSchedulerManager();
        schedular.buildShutdownTask(() -> {
            System.out.println("Server shutting down");
            instCon.saveChunksToStorage();
            instMan.getInstances().forEach(Instance::saveChunksToStorage);
            shutdown();
        });
        startSrv();
        startup();
    }
}
