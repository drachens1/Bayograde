package org.drachens.dataClasses.territories;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.events.CaptureBlockEvent;
import org.drachens.events.StartWarEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Province implements Serializable {
    private final Instance instance;
    private final Pos pos;
    private final List<Troop> troops = new ArrayList<>();
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
    private Building building;
    private Country occupier;
    private boolean capturable = true;
    private Material material;
    private boolean city;
    private List<Province> neighbours;
    private Component description;
    private Component secretDescription; //Higher lvls can see this like allys and the ppl in the occupiers country
    private boolean outdatedDescriptions = true;

    public Province(Pos pos, Instance instance, Country occupier, List<Province> neighbours) {
        this.pos = pos;
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.occupier = occupier;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Province(int x, int y, int z, Instance instance, Country occupier, List<Province> neighbours) {
        this.pos = new Pos(x, y, z);
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.occupier = occupier;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Province(int x, int y, int z, Instance instance, List<Province> neighbours) {
        this.pos = new Pos(x, y, z);
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.occupier = null;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Province(Pos pos, Instance instance, List<Province> neighbours) {
        this.pos = pos;
        this.instance = instance;
        this.chunk = instance.getChunk(pos.chunkX(), pos.chunkZ());
        this.occupier = null;
        provinceManager = ContinentalManagers.world(instance).provinceManager();
        this.neighbours = neighbours;
    }

    public Component getDescription(CPlayer p) {
        Country country = p.getCountry();
        if (outdatedDescriptions) {
            description = createPublicDescription();
            secretDescription = createSecretDescription();
        }
        if (country != null && (country == occupier || country.isAlly(occupier))) {
            return secretDescription;
        }

        return description;
    }

    private Component createPublicDescription() {
        return createProvinceDescription();
    }

    private Component createSecretDescription() {
        if (isCity()) {
            if (occupier.getCapital() == this)
                return createSecretCapitalDescription();
            return createSecretCityDescription();
        }
        return createSecretProvinceDescription();
    }

    private final Component unoccupied = Component.text()
            .append(Component.text("_______/", NamedTextColor.BLUE))
            .append(Component.text("Unoccupied", TextColor.color(51, 129, 255)))
            .append(Component.text("\\_______", NamedTextColor.BLUE))
            .build();
    private Component createProvinceDescription() {
        if (occupier==null)return unoccupied;
        return Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text("Province", TextColor.color(51, 129, 255)))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Occupier: "))
                .append(occupier.getNameComponent())
                .appendNewline()
                .appendNewline()
                .build();
    }

    private Component createSecretCapitalDescription() {
        List<Component> comps = new ArrayList<>();
        if (building != null) {
            comps.add(Component.text()
                    .append(Component.text("Building: "))
                    .append(Component.text(building.getBuildTypes().name()))
                    .append(Component.text(" lvl: "))
                    .append(Component.text(building.getCurrentLvl()))
                    .build());
        } else {
            comps.add(Component.text()
                    .append(Component.text("Building: "))
                    .append(Component.text("empty"))
                    .build());
        }

        return Component.text()
                .append(Component.text("________/", NamedTextColor.BLUE))
                .append(Component.text("Capital", TextColor.color(51, 129, 255)))
                .append(Component.text("\\________", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Occupier: "))
                .append(occupier.getNameComponent())
                .appendNewline()
                .append(comps)
                .appendNewline()
                .build();
    }

    private Component createSecretCityDescription() {
        List<Component> comps = new ArrayList<>();
        if (building != null) {
            comps.add(Component.text()
                    .append(Component.text("Building: "))
                    .append(Component.text(building.getBuildTypes().name()))
                    .append(Component.text(" lvl: "))
                    .append(Component.text(building.getCurrentLvl()))
                    .build());
        } else {
            comps.add(Component.text()
                    .append(Component.text("Building: "))
                    .append(Component.text("empty"))
                    .build());
        }
        return Component.text()
                .append(Component.text("__________/", NamedTextColor.BLUE))
                .append(Component.text("City", TextColor.color(51, 129, 255)))
                .append(Component.text("\\_________", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Occupier: "))
                .append(occupier.getNameComponent())
                .appendNewline()
                .append(comps)
                .appendNewline()
                .build();
    }

    private Component createSecretProvinceDescription() {
        List<Component> comps = new ArrayList<>();
        if (building != null) {
            comps.add(Component.text()
                    .append(Component.text("Building: "))
                    .append(Component.text(building.getBuildTypes().name()))
                    .append(Component.text(" lvl: "))
                    .append(Component.text(building.getCurrentLvl()))
                    .build());
        } else {
            comps.add(Component.text()
                    .append(Component.text("Building: "))
                    .append(Component.text("empty"))
                    .build());
        }
        return Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text("Province", TextColor.color(51, 129, 255)))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Occupier: "))
                .append(occupier.getNameComponent())
                .appendNewline()
                .append(comps)
                .appendNewline()
                .build();
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
        outdatedDescriptions = true;
        if (occupier != null) {
            occupier.removeOccupied(this);
            if (isCity())
                this.occupier.cityCaptured(attacker, this);
        }
        if (building != null) {
            building.capture(attacker);
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
        updateBorders();
    }

    public void initialOccupier(Country occupier) {
        this.occupier = occupier;
        occupier.addOccupied(this);
        updateBorders();
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

    public void removeTroop(Troop troop) {
        troops.remove(troop);
    }

    public void capture(Country attacker) {
        if (!attacker.atWar(occupier))
            EventDispatcher.call(new StartWarEvent(attacker, occupier));
        occupier.sendActionBar(Component.text("You have been attacked at " + pos, NamedTextColor.RED));
        EventDispatcher.call(new CaptureBlockEvent(attacker, this.occupier, this));
        if (building != null) this.building.capture(attacker);
        setOccupier(attacker);
        updateBorders();
    }

    public Building getBuilding() {
        return building;
    }

    public double distance(Province province) {
        return getPos().distance(province.getPos());
    }

    public void setBlock(Material block) {
        material = block;
        instance.setBlock(pos, block.block());
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

    public void setCity(Material material) {
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

    public List<Province> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Province> provinces) {
        this.neighbours = provinces;
    }

    public void addBuilding(Building building) {
        this.building = building;
    }

    public void removeBuilding() {
        building = null;
    }
}
