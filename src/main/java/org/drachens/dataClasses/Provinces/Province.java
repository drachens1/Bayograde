package org.drachens.dataClasses.Provinces;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;
import org.drachens.events.CaptureBlockEvent;
import org.drachens.events.StartWarEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class Province implements Serializable {
    private final Instance instance;
    private final Pos pos;
    private final List<Troop> troops = new ArrayList<>();
    private final List<Country> core;
    private final Chunk chunk;
    private final ProvinceManager provinceManager;
    private final Material[] cities = {Material.CYAN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
            Material.YELLOW_GLAZED_TERRACOTTA, Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK};
    private final Pos[] directions = {
            new Pos(-1, 0, 0), // West
            new Pos(1, 0, 0),  // East
            new Pos(0, 0, -1), // North
            new Pos(0, 0, 1),   // South
            new Pos(0, 0, 0) // current
    };
    private final int[][] directions2 = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
    };
    private Country occupier;
    private boolean capturable = true;
    private PlaceableBuilds buildType;
    private Material material;
    private boolean city;
    private List<Province> neighbours;

    public Province(Pos pos, Instance instance, List<Country> cores, Country occupier, List<Province> neighbours) {
        this.pos = pos;
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.core = cores;
        this.occupier = occupier;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Province(int x, int y, int z, Instance instance, List<Country> cores, Country occupier, List<Province> neighbours) {
        this.pos = new Pos(x, y, z);
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.core = cores;
        this.occupier = occupier;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Province(int x, int y, int z, Instance instance, List<Province> neighbours) {
        this.pos = new Pos(x, y, z);
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.core = new ArrayList<>();
        this.occupier = null;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Province(Pos pos, Instance instance, List<Province> neighbours) {
        this.pos = pos;
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.core = new ArrayList<>();
        this.occupier = null;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Pos getPos() {
        return pos;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public Instance getInstance() {
        return instance;
    }

    public Boolean isCapturable() {
        return capturable;
    }

    public Country getOccupier() {
        return occupier;
    }

    public void setOccupier(Country attacker) {
        if (occupier != null) {
            occupier.removeOccupied(this);
            if (isCity())
                this.occupier.cityCaptured(attacker,this);

            if (buildType != null) {
                if (buildType instanceof PlaceableFactory pl) {
                    occupier.removePlaceableFactory(pl);
                }
            }
        }
        this.occupier = attacker;
        occupier.addOccupied(this);
        if (isCity()) {
            if (attacker.isMajorCity(this))
                this.setCity(attacker.getMajorCity(this));
            else
                setCity(1);
            occupier.addCity(this);
        }
        if (buildType != null) {
            if (buildType instanceof PlaceableFactory pl) {
                occupier.addPlaceableFactory(pl);
            }
        }
        updateBorders();
    }

    public void initialOccupier(Country occupier){
        this.occupier = occupier;
        occupier.addOccupied(this);
        updateBorders();
    }
    public List<Country> getCore() {
        return core;
    }

    public void addCore(Country country) {
        core.add(country);
    }

    public void removeCore(Country country) {
        core.remove(country);
    }

    public void setCapturable(Boolean choice) {
        capturable = choice;
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public void addTroop(Troop troop) {
        troops.add(troop);
    }

    public void capture(Country attacker) {
        if (!attacker.atWar(occupier))
            EventDispatcher.call(new StartWarEvent(attacker,occupier));
        occupier.sendActionBar(compBuild("You have been attacked at "+pos, NamedTextColor.RED));
        EventDispatcher.call(new CaptureBlockEvent(attacker, this.occupier, this));
        if (buildType != null) this.buildType.onCaptured(attacker);
        setOccupier(attacker);
        updateBorders();
    }

    public PlaceableBuilds getBuildType() {
        return buildType;
    }

    public void setBuildType(PlaceableBuilds buildType) {
        this.buildType = buildType;
    }

    public double distance(Province province) {
        return getPos().distance(province.getPos());
    }

    public void setBlock(Material block) {
        material = block;
        instance.setBlock(pos, block.block());
    }

    public void setBorder(Material border) {
        material = border;
        instance.setBlock(pos, border.block());
    }

    public void setBlock() {
        material = occupier.getBlock();
        instance.setBlock(pos, occupier.getBlock().block());
    }

    public void setBorder() {
        material = occupier.getBorder();
        instance.setBlock(pos, occupier.getBorder().block());
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isCity() {
        return city;
    }

    public void setCity(boolean city) {
        this.city = city;
    }
    public void setCity(Material material){
        this.setBlock(material);
        Arrays.stream(cities).toList().indexOf(material);
    }

    //6 = capital
    public void setCity(int lvl) {
        this.city = true;
        this.setBlock(cities[lvl]);
        material = cities[lvl];
    }

    private void updateBorders() {

        for (Pos direction : directions) {
            Pos newLoc = this.getPos().add(direction);
            change(newLoc);
        }
    }

    private void change(Pos loc) {
        Province province = provinceManager.getProvince(loc);
        if (province == null || province.isCity()) return;
        Country country = province.getOccupier();
        if (country != null) {


            for (int[] direction2 : directions2) {
                int offsetX2 = direction2[0];
                int offsetY2 = direction2[1];

                Pos neighborLocation = loc.add(offsetX2, 0, offsetY2);

                Province neighbourBlock = provinceManager.getProvince(neighborLocation);
                if (neighbourBlock != null && neighbourBlock.isCapturable() && neighbourBlock.getOccupier() != country) {
                    province.setBorder();
                    return;
                }
            }
            province.setBlock();
        }
    }
    public void setNeighbours(List<Province> provinces){
        this.neighbours = provinces;
    }
    public List<Province> getNeighbours(){
        return neighbours;
    }
}
