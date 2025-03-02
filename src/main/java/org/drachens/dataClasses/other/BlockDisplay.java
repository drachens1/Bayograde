package org.drachens.dataClasses.other;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.utils.PacketSendingUtils;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockDisplay extends Clientside {
    private final EntityMetaDataPacket entityMetaDataPacket;
    private final SpawnEntityPacket spawnEntityPacket;
    private final DestroyEntitiesPacket destroyEntitiesPacket;
    public BlockDisplay(Pos pos, Instance instance, Block block) {
        super(instance,pos);
        spawnEntityPacket=new SpawnEntityPacket(getEntityId(),getUuid(),15,pos,0f,0, (short) 0, (short) 0, (short) 0);

        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        map.put(23,Metadata.BlockState(block));

        entityMetaDataPacket = new EntityMetaDataPacket(getEntityId(),map);
        destroyEntitiesPacket = new DestroyEntitiesPacket(getEntityId());
    }

    public BlockDisplay(Pos pos, Instance instance, Block block, boolean glowing) {
        super(instance,pos);
        spawnEntityPacket=new SpawnEntityPacket(getEntityId(),getUuid(),15,pos,0f,0, (short) 0, (short) 0, (short) 0);

        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        map.put(23,Metadata.BlockState(block));
        map.put(12,Metadata.Vector3(new Pos(1,0.99,1)));
        map.put(0, Metadata.Byte((byte) 0x40));

        entityMetaDataPacket = new EntityMetaDataPacket(getEntityId(),map);
        destroyEntitiesPacket = new DestroyEntitiesPacket(getEntityId());
    }

    @Override
    public void addCountry(Country country) {
        List<Player> p = new ArrayList<>(country.getInfo().getPlayers());
        p.forEach(player -> addPlayer((CPlayer) player));

        PacketSendingUtils.sendGroupedPacket(p,spawnEntityPacket);
        PacketSendingUtils.sendGroupedPacket(p,entityMetaDataPacket);
    }

    @Override
    public void removeCountry(Country country) {
        List<Player> p = new ArrayList<>(country.getInfo().getPlayers());
        p.forEach(player -> removePlayer((CPlayer) player));

        PacketSendingUtils.sendGroupedPacket(p,destroyEntitiesPacket);
    }

    @Override
    public void addViewer(CPlayer p) {
        addPlayer(p);
        PacketSendingUtils.sendPacket(p,spawnEntityPacket);
        PacketSendingUtils.sendPacket(p,entityMetaDataPacket);
    }

    @Override
    public void removeViewer(CPlayer p) {
        removePlayer(p);
        PacketSendingUtils.sendPacket(p,destroyEntitiesPacket);
    }

    @Override
    public SpawnEntityPacket getSpawnPacket() {
        return spawnEntityPacket;
    }

    @Override
    public DestroyEntitiesPacket getDestroyPacket() {
        return destroyEntitiesPacket;
    }
}
