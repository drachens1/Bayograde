package org.drachens;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.drachens.Manager.WhitelistManager;
import org.drachens.cmd.ListCMD;
import org.drachens.cmd.ban.BanCMD;
import org.drachens.cmd.ban.UnbanCMD;
import org.drachens.cmd.help.HelpCMD;
import org.drachens.cmd.operator;
import org.drachens.cmd.whitelist.WhitelistCMD;

import static org.drachens.Manager.ConfigFileManager.*;
import static org.drachens.api.util.Messages.*;
import static org.drachens.api.util.PermissionsUtil.setupPerms;

public class Main {
    public static void main(String[] args) {
        //Initialize the server
        MinecraftServer srv = MinecraftServer.init();

        //Create the instance(world)
        InstanceManager instMan = MinecraftServer.getInstanceManager();
        InstanceContainer instCon = instMan.createInstanceContainer();

        //Generate the world
        instCon.setGenerator(unit -> unit.modifier().fillHeight(0,40, Block.GRASS_BLOCK));

        //lighting
        instCon.setChunkSupplier(LightingChunk::new);

        //Event handler to manage player spawning
        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();

        globEHandler.addListener(PlayerBlockBreakEvent.class,e -> e.setCancelled(true));

        //Mojang auth so online mode
        MojangAuth.init();

        startup();
        setupPerms();

        globEHandler.addListener(AsyncPlayerConfigurationEvent.class,e -> {
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

        globEHandler.addListener(PlayerDisconnectEvent.class,e -> {
            final Player p = e.getPlayer();
            globalBroadcast(p.getUsername()+" has left the game");
            playerSave(p.getUuid());
        });

        globEHandler.addListener(PlayerChatEvent.class,e->{
            final Player p = e.getPlayer();
            logMsg(p.getUsername(),e.getMessage(),p.getInstance());
        });

        globEHandler.addListener(PlayerCommandEvent.class,e->{
            final Player p = e.getPlayer();
            logCmd(p.getUsername(),e.getCommand(),p.getInstance());
        });

        WhitelistManager whitelistManager = new WhitelistManager();

        //Register cmds
        MinecraftServer.getCommandManager().register(new HelpCMD());
        MinecraftServer.getCommandManager().register(new operator());
        MinecraftServer.getCommandManager().register(new BanCMD());
        MinecraftServer.getCommandManager().register(new UnbanCMD());
        MinecraftServer.getCommandManager().register(new ListCMD());
        MinecraftServer.getCommandManager().register(new WhitelistCMD(whitelistManager));


        var schedular = MinecraftServer.getSchedulerManager();
        schedular.buildShutdownTask(() -> {
            System.out.println("Server shutting down");
            instCon.saveChunksToStorage();
            instMan.getInstances().forEach(Instance::saveChunksToStorage);
            shutdown();
        });
        //Starts the server
        srv.start("0.0.0.0",25565);
    }
}