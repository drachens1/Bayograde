package org.drachens.dataClasses.other;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.utils.PacketSendingUtils;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.animation.AnimationType;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import java.util.*;
import java.util.function.Consumer;

@Getter
@Setter
@SuperBuilder
public abstract class Clientside {
    public static final List<Clientside> INSTANCES = new ArrayList<>();
    public final int entityId;
    public final BoundingBox boundingBox;
    public final UUID uuid;
    public final Instance instance;
    private final List<CPlayer> VIEWERS = new ArrayList<>();
    public Pos pos;
    private AnimationType animation;
    private final List<InteractionEntity> interactionEntity = new ArrayList<>();

    protected Clientside(Instance instance, Pos pos) {
        this.entityId = Entity.generateId();
        this.uuid = UUID.randomUUID();
        this.instance = instance;
        this.pos = pos;
        this.boundingBox = new BoundingBox(
                pos.x() - 0.5, pos.y(), pos.z() - 0.5
        );
    }

    protected Clientside(Instance instance, Pos pos, float hitBoxHeight, float hitBoxWidth, Pos offset, Consumer<CPlayer> consumer) {
        this.entityId = Entity.generateId();
        this.uuid = UUID.randomUUID();
        this.instance = instance;
        this.pos = pos;
        this.boundingBox = new BoundingBox(
                pos.x() - 0.5, pos.y(), pos.z() - 0.5
        );
        this.interactionEntity.add(new InteractionEntity(this,hitBoxHeight,hitBoxWidth, consumer,offset));
    }

    public abstract void addCountry(Country country);

    public abstract void removeCountry(Country country);

    public abstract void addViewer(CPlayer p);

    public abstract void removeViewer(CPlayer p);

    public void dispose() {
        INSTANCES.remove(this);
        new ArrayList<>(VIEWERS).forEach(this::removeViewer);
        if (interactionEntity!=null){
            interactionEntity.forEach(InteractionEntity::dispose);
        }
    }

    public abstract SpawnEntityPacket getSpawnPacket();

    public abstract DestroyEntitiesPacket getDestroyPacket();

    public void addPlayer(CPlayer p) {
        VIEWERS.add(p);
        if (interactionEntity!=null){
            interactionEntity.forEach(i->i.addPlayer(p));
        }
    }

    public void removePlayer(CPlayer p) {
        VIEWERS.remove(p);
        if (interactionEntity!=null){
            interactionEntity.forEach(i->i.removePlayer(p));
        }
    }

    public void addPlayers(List<CPlayer> p) {
        VIEWERS.addAll(p);
        if (interactionEntity!=null){
            interactionEntity.forEach(i->p.forEach(i::addPlayer));
        }
    }

    public void removePlayers(List<CPlayer> p) {
        VIEWERS.removeAll(p);
        if (interactionEntity!=null){
            interactionEntity.forEach(i->p.forEach(i::removePlayer));
        }
    }

    public List<Player> getAsPlayers() {
        return new ArrayList<>(VIEWERS);
    }

    public void addBoundingBox(float hitBoxWidth, float hitBoxHeight, Pos offset, Consumer<CPlayer> consumer){
        this.interactionEntity.add(new InteractionEntity(this,hitBoxHeight,hitBoxWidth, consumer,offset));
    }

    public static class InteractionEntity{
        private final Consumer<CPlayer> onInteract;
        private final SpawnEntityPacket spawnEntityPacket;
        private final EntityMetaDataPacket entityMetaDataPacket;
        private final DestroyEntitiesPacket destroyEntitiesPacket;
        private final List<Player> players = new ArrayList<>();
        @Getter
        private final int entityId;

        public InteractionEntity(Clientside clientside, float hitBoxHeight, float hitBoxWidth, Consumer<CPlayer> onInteract, Pos offset){
            this.onInteract=onInteract;
            UUID uuid = UUID.randomUUID();
            entityId = Entity.generateId();
            this.spawnEntityPacket = new SpawnEntityPacket(
                    entityId,
                    uuid,
                    EntityType.INTERACTION.id(),
                    clientside.pos.add(offset),
                    0f, 0, (short) 0, (short) 0, (short) 0
            );
            Map<Integer, Metadata.Entry<?>> entries = new HashMap<>();
            entries.put(8,Metadata.Float(hitBoxWidth));
            entries.put(9,Metadata.Float(hitBoxHeight));
            this.entityMetaDataPacket = new EntityMetaDataPacket(
                    entityId,
                    entries
            );
            this.destroyEntitiesPacket = new DestroyEntitiesPacket(entityId);
            ContinentalManagers.interactableEntityManager.register(this);
        }

        public void interact(CPlayer p){
            onInteract.accept(p);
        }

        public void addPlayer(Player player){
            PacketSendingUtils.sendPacket(player,spawnEntityPacket);
            PacketSendingUtils.sendPacket(player,entityMetaDataPacket);
            players.add(player);
        }

        public void removePlayer(Player player){
            PacketSendingUtils.sendPacket(player,destroyEntitiesPacket);
            players.add(player);
        }

        public void dispose(){
            players.forEach(this::removePlayer);
            ContinentalManagers.interactableEntityManager.unRegister(this);
        }
    }
}
