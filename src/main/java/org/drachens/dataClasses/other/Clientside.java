package org.drachens.dataClasses.other;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.drachens.util.ServerUtil.getWorldClasses;

public abstract class Clientside {
    public static final List<Clientside> INSTANCES = new ArrayList<>();
    public final List<Player> VIEWERS = new ArrayList<>();
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
        getWorldClasses(instance).clientEntsToLoad().addClientSide(instance, this);
    }

    public abstract void addCountry(Country country);

    public abstract void removeCountry(Country country);

    public abstract void addViewer(Player p);

    public abstract void removeViewer(Player p);

    public void dispose() {
        INSTANCES.remove(this);
        if (storeViewers)
            new ArrayList<>(VIEWERS).forEach(this::removeViewer);

    }
}
