package org.drachens.dataClasses.other;

import dev.ng5m.CPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Clientside {
    public static final List<Clientside> INSTANCES = new ArrayList<>();
    private final List<CPlayer> VIEWERS = new ArrayList<>();
    private final List<CPlayer> visible = new ArrayList<>();
    public final int entityId;
    public final UUID uuid;
    public final Instance instance;
    public boolean storeViewers;
    public Pos pos;

    public Clientside(boolean storeViewers, Instance instance, Pos pos) {
        this.storeViewers = storeViewers;
        this.entityId = Entity.generateId();
        this.uuid = UUID.randomUUID();
        this.instance = instance;
        this.pos = pos;
    }

    public abstract void addCountry(Country country);

    public abstract void removeCountry(Country country);

    public abstract void addViewer(CPlayer p);

    public abstract void removeViewer(CPlayer p);

    public void dispose() {
        INSTANCES.remove(this);
        if (storeViewers)
            new ArrayList<>(VIEWERS).forEach(this::removeViewer);

    }

    public abstract SpawnEntityPacket getSpawnPacket();
    public abstract DestroyEntitiesPacket getDestroyPacket();

    public void addPlayer(CPlayer p){
        p.addClientside(this);
        VIEWERS.add(p);
        visible.add(p);
    }
    public void removePlayer(CPlayer p){
        p.removeClientside(this);
        VIEWERS.remove(p);
        visible.remove(p);
    }
    public void addPlayers(List<CPlayer> p){
        p.forEach(cPlayer -> cPlayer.addClientside(this));
        VIEWERS.addAll(p);
        visible.addAll(p);
    }
    public void removePlayers(List<CPlayer> p){
        p.forEach(cPlayer -> cPlayer.removeClientside(this));
        VIEWERS.removeAll(p);
        visible.removeAll(p);
    }
    public List<CPlayer> getViewers(){
        return VIEWERS;
    }
    public List<Player> getAsPlayers(){
        return new ArrayList<>(VIEWERS);
    }
    public boolean isVisible(CPlayer p){
        return visible.contains(p);
    }
    public void addVisible(CPlayer p){
        visible.add(p);
    }
    public void removeVisible(CPlayer p){
        visible.remove(p);
    }
}
