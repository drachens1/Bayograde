package org.drachens.Manager;

import dev.ng5m.CPlayer;
import dev.ng5m.Rank;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.World;
import org.drachens.fileManagement.PlayerInfoEntry;
import org.drachens.fileManagement.databases.Table;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.function.Function;

public class WorldManager {
    private World defaultWorld;
    private final HashMap<Instance, World> worldHashMap = new HashMap<>();
    private final HashMap<CPlayer, Instance> playerHashSet = new HashMap<>();
    private final Function<Player, Component> displayNameSupplier = Player::getName;
    private final Rank r = new Rank(displayNameSupplier, Component.text("cool", NamedTextColor.BLUE), Component.text("cool2", NamedTextColor.BLUE), NamedTextColor.RED, "cool");

    public WorldManager() {
        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();
        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> worldHashMap.get(e.getInstance()).playerBlockInteract(e));
        globEHandler.addListener(PlayerUseItemEvent.class, e -> worldHashMap.get(e.getInstance()).playerUseItem(e));
        globEHandler.addListener(PlayerStartDiggingEvent.class, e -> worldHashMap.get(e.getInstance()).playerStartDigging(e));
        globEHandler.addListener(PlayerMoveEvent.class, e -> worldHashMap.get(e.getInstance()).playerMove(e));
        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            if (worldHashMap.containsKey(e.getInstance())){
                worldHashMap.get(e.getInstance()).playerDisconnect(e);
            }
        });
        globEHandler.addListener(PlayerHandAnimationEvent.class, e -> worldHashMap.get(e.getInstance()).playerAnimationEvent(e));
        globEHandler.addListener(PlayerSpawnEvent.class, e -> {
            CPlayer p = (CPlayer) e.getPlayer();
            if (playerHashSet.containsKey(p)) {
                worldHashMap.get(playerHashSet.get(p)).removePlayer(p);
            } else {
                initialJoin(p);
            }
            playerHashSet.put(p, p.getInstance());
            worldHashMap.get(p.getInstance()).addPlayer(p);
        });
    }

    public void initialJoin(CPlayer p) {
        Table table = ContinentalManagers.database.getTable("player_info");
        new PlayerInfoEntry(p, table);
        r.addPlayer(p);
        ContinentalManagers.permissions.playerOp(p);
        p.getInstance().enableAutoChunkLoad(false);
        p.setAllowFlying(true);
        p.refreshCommands();
        ContinentalManagers.advancementManager.addPlayer(p);
        p.setJoinTime(LocalTime.now());
        p.setHead();
        try {
            p.sendResourcePacks(ResourcePackRequest.addingRequest(ResourcePackInfo.resourcePackInfo(p.getUuid(),new URI("https://download.mc-packs.net/pack/e12c2a17430747a9bb58cce2af34aef1aebac149.zip"),"e12c2a17430747a9bb58cce2af34aef1aebac149")));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultWorld(World world) {
        this.defaultWorld = world;
    }

    public World getDefaultWorld() {
        return defaultWorld;
    }

    public void registerWorld(World world) {
        worldHashMap.put(world.getInstance(), world);
    }

    public void unregisterWorld(World world) {
        worldHashMap.remove(world.getInstance());
    }

    public World getWorld(Instance instance){
        return worldHashMap.get(instance);
    }
}
