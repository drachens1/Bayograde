package org.drachens.dataClasses;

import dev.ng5m.CPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.utils.PacketUtils;
import org.drachens.Manager.defaults.ContinentalManagers;

import java.util.HashMap;
import java.util.HashSet;

public class ImaginaryWorld {
    private final Instance instance;
    private final HashMap<Chunk, HashMap<Pos, BlockChangePacket>> ghostBlocksHashMap = new HashMap<>();
    private final HashSet<Player> players = new HashSet<>();
    public ImaginaryWorld(Instance instance){
        this.instance=instance;
    }

    public void addPlayer(CPlayer p){
        players.add(p);
        ContinentalManagers.imaginaryWorldManager.addPlayers(p,this);
        ghostBlocksHashMap.forEach((chunk, ghosty) -> ghosty.forEach((pos,block)-> PacketUtils.sendPacket(p,block)));
    }

    public void removePlayer(CPlayer p){
        players.remove(p);
        ContinentalManagers.imaginaryWorldManager.removePlayers(p,this);
    }

    public HashSet<Player> getPlayers(){
        return players;
    }

    public void addGhostBlock(Pos pos, Block block){
        Chunk chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.getOrDefault(chunk,new HashMap<>());
        BlockChangePacket blockChangePacket = new BlockChangePacket(pos,block);
        if (ghosty.containsKey(pos)){
            PacketUtils.sendGroupedPacket(players,blockChangePacket);
        }
        ghosty.put(pos,blockChangePacket);
        ghostBlocksHashMap.put(chunk,ghosty);
    }

    public void removeGhostBlock(Pos pos){
        Chunk chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.getOrDefault(chunk,new HashMap<>());
        if (ghosty.containsKey(pos)){
            Block block = instance.getBlock(pos);
            PacketUtils.sendGroupedPacket(players,new BlockChangePacket(pos,block));
        }
        ghosty.remove(pos);
        ghostBlocksHashMap.put(chunk,ghosty);
    }

    public void loadChunk(Player p, Chunk chunk){
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.get(chunk);
        if (ghosty==null)return;
        ghosty.forEach((pos,packet)->PacketUtils.sendPacket(p,packet));
    }

    public Instance getInstance(){
        return instance;
    }
}
