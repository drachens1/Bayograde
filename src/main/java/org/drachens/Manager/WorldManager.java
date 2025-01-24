package org.drachens.Manager;

import dev.ng5m.CPlayer;
import dev.ng5m.Rank;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.function.Function;

public class WorldManager {
    private final PlayerModsManager playerModsManager;
    private final HashMap<Instance, World> worldHashMap = new HashMap<>();
    private final HashMap<CPlayer, Instance> playerHashSet = new HashMap<>();
    private final Function<Player, Component> displayNameSupplier = Player::getName;
    private final Rank defaultRank = new Rank(displayNameSupplier, Component.text("", NamedTextColor.GRAY, TextDecoration.BOLD), Component.text(""), NamedTextColor.GRAY, "default");
    private final ResourcePackInfo resourcePackInfo = ResourcePackInfo.resourcePackInfo()
            .uri(URI.create("https://download.mc-packs.net/pack/60f880067ca235c61abbc7949269cba5a0f0d01e.zip"))
            .hash("60f880067ca235c61abbc7949269cba5a0f0d01e").build();
    private World defaultWorld;

    public WorldManager() {
        playerModsManager = ContinentalManagers.playerModsManager;
        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();
        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> worldHashMap.get(e.getInstance()).playerBlockInteract(e));
        globEHandler.addListener(PlayerUseItemEvent.class, e -> worldHashMap.get(e.getInstance()).playerUseItem(e));
        globEHandler.addListener(PlayerStartDiggingEvent.class, e -> worldHashMap.get(e.getInstance()).playerStartDigging(e));
        globEHandler.addListener(PlayerMoveEvent.class, e -> worldHashMap.get(e.getInstance()).playerMove(e));
        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            if (worldHashMap.containsKey(e.getInstance())) {
                worldHashMap.get(e.getInstance()).playerDisconnect(e);
                CPlayer p = (CPlayer) e.getPlayer();
                if (p.isUsingMod()) playerModsManager.removePlayer(p, e.getInstance());
            }
        });
        globEHandler.addListener(PlayerHandAnimationEvent.class, e -> worldHashMap.get(e.getInstance()).playerAnimationEvent(e));
        globEHandler.addListener(PlayerSpawnEvent.class, e -> {
            CPlayer p = (CPlayer) e.getPlayer();
            if (playerHashSet.containsKey(p)) {
                Instance prev = playerHashSet.get(p);
                worldHashMap.get(prev).removePlayer(p);
                if (p.isUsingMod()) playerModsManager.removePlayer(p, prev);
            } else {
                initialJoin(p);
            }
            if (p.isUsingMod()) playerModsManager.putPlayer(p, e.getInstance());
            playerHashSet.put(p, p.getInstance());
            worldHashMap.get(p.getInstance()).addPlayer(p);
        });
    }

    public void initialJoin(CPlayer p) {
        Table table = ContinentalManagers.database.getTable("player_info");
        new PlayerInfoEntry(p, table);
        defaultRank.addPlayer(p);
        ContinentalManagers.permissions.playerOp(p);
        p.getInstance().enableAutoChunkLoad(false);
        p.setAllowFlying(true);
        p.refreshCommands();
        ContinentalManagers.advancementManager.addPlayer(p);
        p.setJoinTime(LocalTime.now());
        p.setHead();
        sendResourcePack(p);
        p.sendPluginMessage("continentalmod", "joined");
    }

    private void sendResourcePack(CPlayer p) {
        final ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                .packs(resourcePackInfo)
                .prompt(Component.text("Please download the resource pack!"))
                .required(true)
                .build();

        //p.sendResourcePacks(request);
    }

    public World getDefaultWorld() {
        return defaultWorld;
    }

    public void setDefaultWorld(World world) {
        this.defaultWorld = world;
    }

    public void registerWorld(World world) {
        worldHashMap.put(world.getInstance(), world);
    }

    public void unregisterWorld(World world) {
        worldHashMap.remove(world.getInstance());
    }

    public World getWorld(Instance instance) {
        return worldHashMap.get(instance);
    }
}
