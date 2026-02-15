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
    private final HashMap<Province, List<Troop>> troopDistributionHashMap = new HashMap<>();
    @Getter
    private Province atk;
    @Getter
    private boolean atking;
    @Getter
    private final String name;

    public Frontline(String name){
        this.name = name;
        this.provinces = new HashSet<>();
        this.troops = new HashSet<>();
        this.atk = null;
    }

    public void addViewer(CPlayer p){
        troops.forEach(troop -> troop.getTroop().setGlowingForPlayer(true, p, -1));
        clientsides.forEach(clientside -> clientside.addViewer(p));
        viewers.add(p);
    }

    public void removeViewer(CPlayer p){
        troops.forEach(troop -> troop.getTroop().setGlowingForPlayer(false, p, -1));
        clientsides.forEach(clientside -> clientside.removeViewer(p));
        viewers.remove(p);
    }

    public void addTroop(Troop troop) {
        if (troops.contains(troop)) return;

        Province targetProvince = getProvinceWithLeastTroops();
        if (targetProvince != null) {
            troop.move(targetProvince);
            addTroopToProvince(targetProvince,troop);
        }

        viewers.forEach(player -> troop.getTroop().setGlowingForPlayer(true, player, 1424689));
        troops.add(troop);
    }

    public void addTroopToProvince(Province province, Troop troop){
        troopDistributionHashMap.computeIfAbsent(province, k -> new ArrayList<>()).add(troop);
    }

    public void removeTroopFromProvince(Province province, Troop troop) {
        List<Troop> troops = troopDistributionHashMap.get(province);
        if (null != troops) {
            troops.remove(troop);
        }
    }

    public void removeTroop(Troop troop) {
        viewers.forEach(player -> troop.getTroop().setGlowingForPlayer(false, player, 1424689));
        troops.remove(troop);
        removeTroopFromProvince(troop.getProvince(),troop);
    }

    public void addProvince(Province province) {
        if (provinces.contains(province)) return;
        provinces.add(province);

        BlockDisplay i = new BlockDisplay(province.getPos(), province.getInstance(), province.getMaterial().block(), true);
        clientsideProvince.put(province, i);
        clientsides.add(i);
        viewers.forEach(i::addViewer);

        distributeTroopsEvenly();
    }

    public void removeProvince(Province province) {
        BlockDisplay i = clientsideProvince.get(province);
        if (i == null) return;

        i.dispose();
        provinces.remove(province);

        distributeTroopsEvenly();
    }

    public void distributeTroopsEvenly() {
        if (provinces.isEmpty() || troops.isEmpty()) {
            return;
        }

        troopDistributionHashMap.clear();

        int provinceCount = provinces.size();
        int index = 0;

        for (Troop troop : troops) {
            Province targetProvince = new ArrayList<>(provinces).get(index % provinceCount);
            troop.move(targetProvince);
            index++;
        }
    }

    private Province getProvinceWithLeastTroops() {
        return provinces.stream()
                .min(Comparator.comparingInt(p -> troopDistributionHashMap.getOrDefault(p, new ArrayList<>()).size()))
                .orElse(null);
    }

    public void setAtk(Province province){
        if (province==null)return;
        if (atk!=null){
            BlockDisplay b = clientsideProvince.get(atk);
            clientsides.remove(b);
            b.dispose();
            clientsideProvince.remove(atk);
        }
        BlockDisplay i = new BlockDisplay(province.getPos(), province.getInstance(), province.getMaterial().block(), true, 9639182);
        clientsideProvince.put(province, i);
        viewers.forEach(i::addViewer);
        clientsides.add(i);
        this.atk=province;
    }

    public void removeAtk(Province province){
        if (atk!=province)return;
        BlockDisplay b = clientsideProvince.get(province);
        clientsideProvince.remove(province);
        atk=null;
        if (b==null)return;
        clientsides.remove(b);
        b.dispose();
    }

    public void setAtking(boolean atking){
        this.atking=atking;
        if (atking){
            startAttacking();
        }else {
            stopAttacking();
        }
    }

    private void startAttacking(){
        troops.forEach(this::getAtkForTroop);
    }

    public void getAtkForTroop(Troop troop){
        if (!troops.contains(troop))return;
        Province troopProvince = troop.getProvince();
        double distance = troopProvince.distance(atk);
        Province to = null;
        for (Province neighbour : troopProvince.getNeighbours()){
            if (neighbour.distance(atk)<distance){
                to = neighbour;
            }
        }
        if (to==null)return;
        troop.moveBecauseOfFrontLine(to,this);
    }

    private void stopAttacking(){

    }
}
