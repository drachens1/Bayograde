package org.drachens.dataClasses.Provinces;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;
import org.drachens.events.CaptureBlockEvent;
import org.drachens.events.CountryJoinEvent;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.Messages.globalBroadcast;
import static org.drachens.util.ServerUtil.getProvinceManager;

public class Province {
    private final Instance instance;
    private final Pos pos;
    private final List<Troop> troops = new ArrayList<>();
    private Country occupier;
    private final List<Country> core;
    private boolean capturable = true;
    private final Chunk chunk;
    private final ProvinceManager provinceManager;
    private PlaceableBuilds buildType;
    private Material material;
    private boolean city;
    public Province(Pos pos, Instance instance, List<Country> cores, Country occupier){
        this.pos = pos;
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        this.core = cores;
        this.occupier = occupier;
        provinceManager = getProvinceManager();
    }
    public Province(int x, int y , int z, Instance instance, List<Country> cores, Country occupier){
        this.pos = new Pos(x,y,z);
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        this.core = cores;
        this.occupier = occupier;
        provinceManager = getProvinceManager();
    }
    public Province(int x, int y , int z, Instance instance){
        this.pos = new Pos(x,y,z);
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        this.core = new ArrayList<>();
        this.occupier = null;
        provinceManager = getProvinceManager();
    }
    public Province(Pos pos, Instance instance){
        this.pos = pos;
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(),pos.chunkZ());
        this.core = new ArrayList<>();
        this.occupier = null;
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
    public Boolean isCapturable(){
        return capturable;
    }
    public Country getOccupier(){
        return occupier;
    }
    public void setOccupier(Country occupier){
        Country current = getOccupier();
        if (current != null){
            current.removeOccupied(this);
            if (isCity())current.removeCity(this);
            if (buildType!=null){
                if (buildType instanceof PlaceableFactory pl){
                    current.removePlaceableFactory(pl);
                }
            }
        }
        this.occupier = occupier;
        occupier.addOccupied(this);
        if (isCity())occupier.addCity(this);
        if (buildType!=null){
            if (buildType instanceof PlaceableFactory pl){
                occupier.addPlaceableFactory(pl);
            }
        }
        updateBorders();
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
        EventDispatcher.call(new CaptureBlockEvent(attacker,this.occupier,this));
        if (buildType!=null) this.buildType.onCaptured(attacker);
        setOccupier(attacker);
        updateBorders();
        globalBroadcast("capturedevent");
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
    public void setBlock(Material block){
        instance.setBlock(pos,block.block());
    }
    public void setBorder(Material border){
        instance.setBlock(pos,border.block());
    }
    public void setBlock(){
        instance.setBlock(pos,occupier.getBlock().block());
    }
    public void setBorder(){
        instance.setBlock(pos,occupier.getBorder().block());
    }
    public Material getMaterial(){
        return material;
    }
    public void setMaterial(Material material){
        this.material = material;
    }
    public boolean isCity(){
        return city;
    }
    public void setCity(boolean city) {
        this.city = city;
    }
    private final Material[] cities = {Material.CYAN_GLAZED_TERRACOTTA,Material.GREEN_GLAZED_TERRACOTTA,Material.LIME_GLAZED_TERRACOTTA,
            Material.YELLOW_GLAZED_TERRACOTTA,Material.RAW_GOLD_BLOCK,Material.GOLD_BLOCK,Material.EMERALD_BLOCK};
    //6 = capital
    public void setCity(int lvl) {
        this.city = true;
        this.setBlock(cities[lvl]);
    }
    private void updateBorders(){
        Pos[] directions = {
                new Pos(-1, 0, 0), // West
                new Pos(1, 0, 0),  // East
                new Pos(0, 0, -1), // North
                new Pos(0, 0, 1),   // South
                new Pos(0,0,0) // current
        };
        for (Pos direction : directions){
            Pos newLoc = this.getPos().add(direction);
            change(newLoc);
        }
    }
    private void change(Pos loc){
        Province province = provinceManager.getProvince(loc);
        if (province == null || province.isCity())return;
        Country country = province.getOccupier();
        if (country != null){
            int[][] directions2 = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            };

            for (int[] direction2 : directions2) {
                int offsetX2 = direction2[0];
                int offsetY2 = direction2[1];

                Pos neighborLocation = loc.add(offsetX2, 0, offsetY2);

                Province neighbourBlock = provinceManager.getProvince(neighborLocation);
                if (neighbourBlock != null && neighbourBlock.isCapturable() && neighbourBlock.getOccupier() != country){
                    province.setBorder();
                    return;
                }
            }
            province.setBlock();
        }
    }
}
