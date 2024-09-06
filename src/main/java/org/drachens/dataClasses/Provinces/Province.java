package org.drachens.dataClasses.Provinces;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.PlaceableBuilds;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ServerUtil.getProvinceManager;

public class Province {
    private final Instance instance;
    private final Pos pos;
    private final List<Troop> troops = new ArrayList<>();
    private Country occupier;
    private final List<Country> core;
    private boolean capturable;
    private final Chunk chunk;
    private final ProvinceManager provinceManager;
    private Material material;
    private PlaceableBuilds buildType;

    public Province(Pos pos, Instance instance, List<Country> cores, Country occupier){
        this.pos = pos;
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        this.core = cores;
        this.occupier = occupier;
        this.material = Material.fromId(instance.getBlock(pos).id());
        provinceManager = getProvinceManager();
    }

    public Province(int x, int y , int z, Instance instance, List<Country> cores, Country occupier){
        this.pos = new Pos(x,y,z);
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        this.core = cores;
        this.occupier = occupier;
        this.material = Material.fromId(instance.getBlock(pos).id());
        provinceManager = getProvinceManager();
    }

    public Pos getPos(){
        return pos;
    }
    public Chunk getChunk(){
        return chunk;
    }
    public Instance getInstance(){
        return instance;
    }
    public Material getMaterial(){
        return material;
    }
    public Boolean isCapturable(){
        return capturable;
    }
    public Country getOccupier(){
        return occupier;
    }
    public void setOccupier(Country occupier){
        this.occupier = occupier;
    }
    public List<Country> getCore(){
        return core;
    }
    public void addCore(Country country){
        core.add(country);
    }
    public void removeCore(Country country){
        core.remove(country);
    }
    public void setCapturable(Boolean choice){
        capturable = choice;
    }
    public List<Troop> getTroops(){
        return troops;
    }
    public void addTroop(Troop troop){
        troops.add(troop);
    }
    public void capture(Country attacker){

    }
    public PlaceableBuilds getBuildType(){
        return buildType;
    }
    public void setBuildType(PlaceableBuilds buildType){
        this.buildType = buildType;
    }
    public double distance(Province province){
        return getPos().distance(province.getPos());
    }
}
