package org.drachens.Manager;

import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.dataClasses.World;
import org.drachens.fileManagement.customTypes.player.CustomLoginRecord;
import org.drachens.player_types.CPlayer;
import org.drachens.store.other.LoginMessage;

import java.net.URI;
import java.time.LocalTime;
import java.util.HashMap;

import static org.drachens.util.Messages.broadcast;

public class WorldManager {
    private final PlayerModsManager playerModsManager;
    private final HashMap<Instance, World> worldHashMap = new HashMap<>();
    private final HashMap<CPlayer, Instance> playerHashSet = new HashMap<>();
    private final ResourcePackInfo resourcePackInfo = ResourcePackInfo.resourcePackInfo()
            .uri(URI.create("https://download.mc-packs.net/pack/60f880067ca235c61abbc7949269cba5a0f0d01e.zip"))
            .hash("60f880067ca235c61abbc7949269cba5a0f0d01e").build();

    private final LoginMessage def = new LoginMessage(
            player -> Component.text()
                    .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("+", NamedTextColor.GREEN))
                    .append(Component.text("]", NamedTextColor.GRAY))
                    .build(), player -> Component.text()
            .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
            .append(Component.text("[", NamedTextColor.GRAY))
            .append(Component.text("+", NamedTextColor.GREEN))
            .append(Component.text("]", NamedTextColor.GRAY))
            .build(), player -> Component.text()
            .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
            .append(Component.text("[", NamedTextColor.GRAY))
            .append(Component.text("-", NamedTextColor.RED))
            .append(Component.text("]", NamedTextColor.GRAY))
            .build(),
            player -> Component.text()
                    .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("-", NamedTextColor.GREEN))
                    .append(Component.text("]", NamedTextColor.GRAY))
                    .build()
    );
    private World defaultWorld;

    public WorldManager() {
        playerModsManager = ContinentalManagers.playerModsManager;
        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();
        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> worldHashMap.get(e.getInstance()).playerBlockInteract(e));
        globEHandler.addListener(PlayerUseItemEvent.class, e -> worldHashMap.get(e.getInstance()).playerUseItem(e));
        globEHandler.addListener(PlayerStartDiggingEvent.class, e -> worldHashMap.get(e.getInstance()).playerStartDigging(e));
        globEHandler.addListener(PlayerMoveEvent.class, e -> worldHashMap.get(e.getInstance()).playerMove(e));
        globEHandler.addListener(PlayerHandAnimationEvent.class, e -> worldHashMap.get(e.getInstance()).playerAnimationEvent(e));
        globEHandler.addListener(PlayerDisconnectEvent.class, e -> {
            if (worldHashMap.containsKey(e.getInstance())) {
                worldHashMap.get(e.getInstance()).playerDisconnect(e);
                CPlayer p = (CPlayer) e.getPlayer();
                generalChanges(p);
                if (p.isPremium()&&p.getPlayerJson().isCustomLoginMessageActive()){
                    CustomLoginRecord clr = p.getPlayerJson().getCustomLoginMessage();
                    handlePremiumLogin(p,clr.leave(),clr,p.getInstance(),def.onPlayerLeave(p));
                }else {
                    broadcast(def.onPlayerLeave(p),p.getInstance());
                }
                if (p.isUsingMod()) playerModsManager.removePlayer(p, e.getInstance());
                p.addPlayTime(LocalTime.now());
                p.getPlayerInfoEntry().applyChanges();
            }

        });
        globEHandler.addListener(PlayerSpawnEvent.class, e -> {
            CPlayer p = (CPlayer) e.getPlayer();
            generalChanges(p);
            if (playerHashSet.containsKey(p)) {
                Instance prev = playerHashSet.get(p);
                worldHashMap.get(prev).removePlayer(p);
                if (p.isPremium()&&p.getPlayerJson().isCustomLoginMessageActive()){
                    CustomLoginRecord clr = p.getPlayerJson().getCustomLoginMessage();
                    handlePremiumLogin(p,clr.changeLeave(),clr,prev, def.onPlayerChangeInstanceFrom(p));
                    handlePremiumLogin(p,clr.changeJoin(),clr,p.getInstance(), def.onPlayerChangeInstanceTo(p));
                }else {
                    broadcast(def.onPlayerChangeInstanceFrom(p),prev);
                    broadcast(def.onPlayerChangeInstanceTo(p),p.getInstance());
                }
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
        p.setHead();
        p.getPlayerJson().laterInit();
        //sendResourcePack(p);
        ContinentalManagers.permissions.playerOp(p);
        p.getInstance().enableAutoChunkLoad(false);
        p.setAllowFlying(true);
        if (!p.hasRank(RankEnum.default_rank.getRank())){
            RankEnum.default_rank.getRank().addPlayer(p);
        }
        p.refreshCommands();
        ContinentalManagers.advancementManager.addPlayer(p);
        p.setJoinTime(LocalTime.now());
        if (p.isPremium()&&p.getPlayerJson().isCustomLoginMessageActive()){
            CustomLoginRecord clr = p.getPlayerJson().getCustomLoginMessage();
            handlePremiumLogin(p,clr.join(),clr,p.getInstance(), def.onPlayerJoin(p));
        }else {
            broadcast(def.onPlayerJoin(p),p.getInstance());
        }
        if (p.hasPermission("admin")){
            ContinentalManagers.adminManager.addAdmin(p);
        }
        //p.sendPluginMessage("continentalmod", "joined");
    }

    private void handlePremiumLogin(CPlayer p, String msg, CustomLoginRecord clr, Instance instance, Component alt){
        if (msg.isBlank()){
            broadcast(alt,instance);
        }else {
            broadcast(clr.toMessage(msg,p),instance);
        }
    }

    private void sendResourcePack(CPlayer p) {
        final ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                .packs(resourcePackInfo)
                .prompt(Component.text("Please download the resource pack!"))
                .required(true)
                .build();

        //p.sendResourcePacks(request);
    }

    private void generalChanges(CPlayer p){
        if (ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner!=null){
            p.setWorldClasses(ContinentalManagers.world(p.getInstance()));
        }
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
