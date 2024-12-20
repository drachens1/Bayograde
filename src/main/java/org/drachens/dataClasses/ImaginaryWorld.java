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
    }

    public void removePlayer(CPlayer p){
        players.remove(p);
        ContinentalManagers.imaginaryWorldManager.removePlayers(p,this);

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

    public void loadChunk(Player p, Chunk chunk){
        HashMap<Pos, BlockChangePacket> ghosty = ghostBlocksHashMap.get(chunk);
        ghosty.forEach((pos,packet)->PacketUtils.sendPacket(p,packet));
    }

    public Instance getInstance(){
        return instance;
    }
}
