package org.drachens.dataClasses.Armys;

import lombok.Getter;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.BlockDisplay;
import org.drachens.player_types.CPlayer;

import java.util.*;

public class Frontline {
    private final HashMap<Province, BlockDisplay> clientsideProvince = new HashMap<>();
    private final List<BlockDisplay> clientsides = new ArrayList<>();
    private final HashSet<Province> provinces;
    private final HashSet<Troop> troops;
    private final List<CPlayer> viewers = new ArrayList<>();
    @Getter
    private final String name;

    public Frontline(String name){
        this.name=name;
        this.provinces=new HashSet<>();
        this.troops=new HashSet<>();
    }

    public void addViewer(CPlayer p){
        troops.forEach(troop -> troop.getTroop().setGlowingForPlayer(true,p,-1));
        clientsides.forEach(clientside -> clientside.addViewer(p));
        viewers.add(p);
    }

    public void removeViewer(CPlayer p){
        troops.forEach(troop -> troop.getTroop().setGlowingForPlayer(false,p,-1));
        clientsides.forEach(clientside -> clientside.removeViewer(p));
        viewers.remove(p);
    }

    public void addTroop(Troop troop) {
        if (troops.contains(troop))return;
        viewers.forEach(player -> troop.getTroop().setGlowingForPlayer(true,player,1424689));
        troops.add(troop);
    }

    public void removeTroop(Troop troop) {
        viewers.forEach(player -> troop.getTroop().setGlowingForPlayer(false,player,1424689));
        troops.remove(troop);
    }

    public void addProvince(Province province) {
        if (provinces.contains(province))return;
        provinces.add(province);
        BlockDisplay i = new BlockDisplay(province.getPos(),province.getInstance(),province.getMaterial().block(),true );
        clientsideProvince.put(province,i);
        clientsides.add(i);
        viewers.forEach(i::addViewer);
    }

    public void removeProvince(Province province) {
        BlockDisplay i = clientsideProvince.get(province);
        if (i==null)return;
        i.dispose();
        provinces.remove(province);
    }

    public void distributeTroopsEvenly() {
        if (provinces.isEmpty() || troops.isEmpty()) {
            return;
        }

        int provinceCount = provinces.size();
        int index = 0;

        for (Troop troop : troops) {
            Province targetProvince = new ArrayList<>(provinces).get(index % provinceCount);
            targetProvince.addTroop(troop);
            index++;
        }
    }
}
