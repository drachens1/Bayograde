package org.drachens.dataClasses;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.utils.PacketSendingUtils;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;
import java.util.HashSet;

@Getter
public class ImaginaryWorld {
    private final boolean blockUpdatesFromPlayers;
    private final Instance instance;
    private final HashMap<Chunk, HashMap<Pos, BlockChangePacket>> ghostBlocksHashMap = new HashMap<>();
    private final HashSet<Player> players = new HashSet<>();

    public ImaginaryWorld(Instance instance, boolean blockUpdatesFromPlayers) {
        this.instance = instance;
        this.blockUpdatesFromPlayers = blockUpdatesFromPlayers;
    }

    public void addPlayer(CPlayer p) {
        players.add(p);
        ContinentalManagers.imaginaryWorldManager.addPlayers(p, this);
        ghostBlocksHashMap.forEach((chunk, ghosty) -> ghosty.forEach((pos, block) -> PacketSendingUtils.sendPacket(p, block)));
    }

    public void removePlayer(CPlayer p) {
        players.remove(p);
        ContinentalManagers.imaginaryWorldManager.removePlayers(p);
        ghostBlocksHashMap.forEach((chunk, ghosty) -> ghosty.forEach((pos, block) -> PacketSendingUtils.sendPacket(p, new BlockChangePacket(pos, instance.getBlock(pos)))));
    }

    public boolean isThereAGhostBlock(Pos pos) {
        Chunk chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        return ghostBlocksHashMap.containsKey(chunk) && ghostBlocksHashMap.get(chunk).containsKey(pos);
    }

    public void addGhostBlock(Pos pos, Block block) {
        Chunk chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.getOrDefault(chunk, new HashMap<>());
        BlockChangePacket blockChangePacket = new BlockChangePacket(pos, block);
        PacketSendingUtils.sendGroupedPacket(players, blockChangePacket);
        ghosty.put(pos, blockChangePacket);
        ghostBlocksHashMap.put(chunk, ghosty);
    }

    public void removeGhostBlock(Pos pos) {
        Chunk chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.getOrDefault(chunk, new HashMap<>());
        if (ghosty.containsKey(pos)) {
            Block block = instance.getBlock(pos);
            PacketSendingUtils.sendGroupedPacket(players, new BlockChangePacket(pos, block));
        }
        ghosty.remove(pos);
        ghostBlocksHashMap.put(chunk, ghosty);
    }

    public void resend(Pos pos) {
        if (players.isEmpty()) return;
        Chunk chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.getOrDefault(chunk, new HashMap<>());
        if (ghosty.containsKey(pos)) {
            PacketSendingUtils.sendGroupedPacket(players, ghosty.get(pos));
        }
    }

    public void loadChunk(Player p, Chunk chunk) {
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.get(chunk);
        if (null == ghosty) return;
        ghosty.forEach((pos, packet) -> PacketSendingUtils.sendPacket(p, packet));
    }

    public BlockChangePacket getGhostBlockPacket(Pos pos) {
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.getOrDefault(instance.getChunk(pos.chunkX(), pos.chunkZ()), new HashMap<>());
        return ghosty.get(pos);
    }
}
