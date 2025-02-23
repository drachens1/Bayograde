package org.drachens.dataClasses;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.events.CaptureBlockEvent;
import org.drachens.generalGame.troops.Combat;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Province implements Saveable {
    private final Instance instance;
    private final Pos pos;
    private final List<Troop> troops = new ArrayList<>();
    private final Material[] cities = {Material.CYAN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
            Material.YELLOW_GLAZED_TERRACOTTA, Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK};
    private final HashSet<Country> corers = new HashSet<>();
    private final Component unoccupied = Component.text()
            .append(Component.text("_______/", NamedTextColor.BLUE))
            .append(Component.text("Unoccupied", TextColor.color(51, 129, 255)))
            .append(Component.text("\\_______", NamedTextColor.BLUE))
            .build();
    private Combat combat;
    private Building building;
    private Country occupier;
    private Material material;
    private boolean city;
    private List<Province> neighbours;
    private boolean isBorder;

    public Province(Pos pos, Instance instance, Country occupier, List<Province> neighbours) {
        this.pos = pos;
        this.instance = instance;
        this.occupier = occupier;
        this.neighbours = neighbours;
    }

    public Province(int x, int y, int z, Instance instance, Country occupier, List<Province> neighbours) {
        this.pos = new Pos(x, y, z);
        this.instance = instance;
        this.occupier = occupier;
        this.neighbours = neighbours;
    }

    public Province(int x, int y, int z, Instance instance, List<Province> neighbours) {
        this.pos = new Pos(x, y, z);
        this.instance = instance;
        this.occupier = null;
        this.neighbours = neighbours;
    }

    public Province(Pos pos, Instance instance, List<Province> neighbours) {
        this.pos = pos;
        this.instance = instance;
        this.occupier = null;
        this.neighbours = neighbours;
    }

    public Component getDescription(CPlayer p) {
        Country country = p.getCountry();
        if (null != country && country.isPlayerLeader(p)) {
            if (country == occupier) {
                return createSecretDescription().append(Component.text()
                                .append(Component.text("[EXTRA]", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to edit your country", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/country edit options"))
                                .build())
                        .append(Component.text().append(Component.text(" [INFO]", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the information options", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/country info options " + occupier.getName())));
            }
            if (country.isAlly(occupier)) {
                return createSecretDescription().append(Component.text()
                        .append(Component.text("[DIPLOMATIC OPTIONS]", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the diplomatic options for the occupier", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.runCommand("/country diplomacy view_options " + occupier.getName()))
                        .build()
                        .append(Component.text().append(Component.text(" [INFO]", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the information options", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/country info options " + occupier.getName()))));
            }
            return createPublicDescription().append(Component.text()
                            .append(Component.text("[DIPLOMATIC OPTIONS]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the diplomatic options for the occupier", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy view_options " + occupier.getName()))
                            .build())
                    .append(Component.text().append(Component.text(" [INFO]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the information options", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country info options " + occupier.getName())));
        }
        return createPublicDescription();
    }

    private Component createPublicDescription() {
        return createProvinceDescription();
    }

    private Component createSecretDescription() {
        if (this.city) {
            if (occupier.getInfo().getCapital() == this) return createSecretCapitalDescription();
            return createSecretCityDescription();
        }
        return createSecretProvinceDescription();
    }

    private Component createProvinceDescription() {
        if (null == this.occupier) return unoccupied;
        return Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text("Province", TextColor.color(51, 129, 255)))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Occupier: "))
                .append(occupier.getInfo().getOriginalName())
                .appendNewline()
                .appendNewline()
                .build();
    }

    private Component createSecretCapitalDescription() {
        List<Component> comps = new ArrayList<>();
        if (null != this.building) {
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
                .append(occupier.getInfo().getOriginalName())
                .appendNewline()
                .append(comps)
                .appendNewline()
                .build();
    }

    private Component createSecretCityDescription() {
        List<Component> comps = new ArrayList<>();
        if (null != this.building) {
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
                .append(occupier.getInfo().getOriginalName())
                .appendNewline()
                .append(comps)
                .appendNewline()
                .build();
    }

    private Component createSecretProvinceDescription() {
        List<Component> comps = new ArrayList<>();
        if (null != this.building) {
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
                .append(occupier.getInfo().getOriginalName())
                .appendNewline()
                .append(comps)
                .appendNewline()
                .build();
    }

    public void setOccupier(Country attacker) {
        attacker.captureProvince(this);
        if (null != this.occupier) {
            occupier.removeOccupied(this);
            if (this.city) this.occupier.cityCaptured(attacker, this);
        }
        if (this.city) {
            if (attacker.getMilitary().containsMajorCityBlock(this))
                this.setCity(attacker.getMilitary().getMajorCityBlock(this));
            else setCity(1);
            attacker.addCity(this);
        }
        if (null != this.building) {
            building.capture(attacker);
        }
        this.occupier = attacker;
        updateBorders();
    }

    public void liberate(Country attacker) {
        if (null != this.occupier) {
            occupier.removeOccupied(this);
            if (this.city) this.occupier.removeCityWithoutHarm(this);
        }
        if (null != this.building) {
            building.capture(attacker);
        }
        this.occupier = attacker;
        attacker.captureProvince(this);
        if (this.city) {
            if (attacker.getMilitary().containsMajorCityBlock(this))
                setCity(attacker.getMilitary().getMajorCityBlock(this));
            else setCity(1);
            attacker.addCity(this);
        }
        updateBorders();
    }

    public void initialOccupier(Country occupier) {
        this.occupier = occupier;
        occupier.addOccupied(this);
    }

    public void addTroop(Troop troop) {
        troops.add(troop);
    }

    public void removeTroop(Troop troop) {
        troops.remove(troop);
    }

    public void capture(Country attacker) {
        EventDispatcher.call(new CaptureBlockEvent(attacker, this.occupier, this));
    }

    public double distance(Province province) {
        return this.pos.distance(province.pos);
    }

    public void setBlock(Material block) {
        material = block;
        instance.setBlock(pos, block.block());
    }

    public void setBlock() {
        isBorder = false;
        setBlock(occupier.getInfo().getBlock());
    }

    public void setBorder() {
        isBorder = true;
        setBlock(occupier.getInfo().getBorder());
    }

    public void setCity(Material material) {
        this.setBlock(material);
        Arrays.stream(cities).toList().indexOf(material);
    }

    //6 = capital
    public void setCity(int lvl) {
        if (!city) if (null != this.occupier) occupier.addCity(this);
        this.city = true;
        this.setBlock(cities[lvl]);
        material = cities[lvl];
    }

    private void updateBorders() {
        List<Province> neigh = new ArrayList<>(neighbours);
        neigh.add(this);
        neigh.forEach(this::updateProv);
    }

    public void updateProv(Province p) {
        if (p.city) return;
        for (Province p2 : p.neighbours) {
            if (p2.occupier != p.occupier) {
                p.setBorder();
                return;
            }
        }
        p.setBlock();
    }

    public void addBuilding(Building building) {
        this.building = building;
    }

    public void removeBuilding() {
        building = null;
    }

    public boolean isThereCombat() {
        return null != this.combat;
    }

    public void setCore(Country country) {
        corers.add(country);
    }

    public boolean isBorder() {
        return isBorder && !this.city;
    }

    public Province add(int x, int z) {
        return ContinentalManagers.world(instance).provinceManager().getProvince((int) (pos.x() + x), (int) (pos.z() + z));
    }

    public JsonElement getActualJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("p",toJson());//province
        if (combat!=null) jsonObject.add("c", combat.toJson());
        JsonArray jsonArray2 = new JsonArray();
        corers.forEach(country -> jsonArray2.add(country.getName()));
        jsonObject.add("cor",jsonArray2);
        return jsonObject;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive((int) pos.x()+","+ (int) pos.z());
    }
}
