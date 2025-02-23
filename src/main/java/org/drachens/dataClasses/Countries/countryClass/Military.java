package org.drachens.dataClasses.Countries.countryClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.bossbars.CapitulationBar;
import org.drachens.dataClasses.ImaginaryWorld;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.Saveable;
import org.drachens.util.AStarPathfinderXZ;

import java.util.*;

@Getter
@Setter
public class Military implements Saveable {
    private final ImaginaryWorld warsWorld;
    private final ImaginaryWorld allyWorld;
    private final List<Province> occupies;
    private final List<Province> cities;
    private final HashMap<String, List<Province>> occupiesOthersCores = new HashMap<>();
    private final HashMap<String, List<Province>> borderProvinces = new HashMap<>();
    private final HashMap<Province, Material> majorCityBlocks = new HashMap<>();
    private final HashSet<String> countryWars = new HashSet<>();
    private final HashSet<String> bordersWars = new HashSet<>();
    private final CapitulationBar capitulationBar = new CapitulationBar();
    private final AStarPathfinderXZ aStarPathfinder;


    public Military(List<Province> occupies,
                    List<Province> cities, HashMap<String, List<Province>> occupiesOthersCores,
                    HashMap<Province, Material> majorCityBlocks, HashSet<String> countryWars,
                    HashSet<String> bordersWars, AStarPathfinderXZ aStarPathfinder, Instance instance) {
        warsWorld = new ImaginaryWorld(instance, true);
        allyWorld = new ImaginaryWorld(instance, true);
        this.occupies = occupies;
        this.cities = cities;
        this.occupiesOthersCores.putAll(occupiesOthersCores);
        this.majorCityBlocks.putAll(majorCityBlocks);
        this.countryWars.addAll(countryWars);
        this.bordersWars.addAll(bordersWars);
        this.aStarPathfinder = aStarPathfinder;
    }

    public void addOccupiedProvince(Province province) {
        if (!occupies.contains(province)) {
            occupies.add(province);
        }
    }

    public void removeOccupiedProvince(Province province) {
        occupies.remove(province);
    }

    public boolean containsOccupiedProvince(Province province) {
        return occupies.contains(province);
    }

    public List<Province> getOccupiedProvinces() {
        return new ArrayList<>(occupies);
    }

    public void addCity(Province province) {
        if (!cities.contains(province)) {
            cities.add(province);
        }
    }

    public void removeCity(Province province) {
        cities.remove(province);
    }

    public boolean containsCity(Province province) {
        return cities.contains(province);
    }

    public List<Province> getCities() {
        return new ArrayList<>(cities);
    }

    public void addOthersCoreProvince(String country, Province province) {
        occupiesOthersCores.computeIfAbsent(country, k -> new ArrayList<>()).add(province);
    }

    public void removeOthersCoreProvince(String country, Province province) {
        occupiesOthersCores.computeIfPresent(country, (key, provinces) -> {
            provinces.remove(province);
            return provinces.isEmpty() ? null : provinces;
        });
    }

    public boolean containsOthersCoreProvince(String country, Province province) {
        return occupiesOthersCores.containsKey(country) && occupiesOthersCores.get(country).contains(province);
    }

    public boolean containsOthersCoreProvince(String country){
        return occupiesOthersCores.containsKey(country);
    }

    public Set<String> getOccupiesOthersCores(){
        return occupiesOthersCores.keySet();
    }

    public List<Province> getOthersCoreProvinces(String country) {
        return occupiesOthersCores.getOrDefault(country, new ArrayList<>());
    }

    public Set<String> getBorders(){
        return borderProvinces.keySet();
    }

    public void addBorder(String country, Province province) {
        borderProvinces.computeIfAbsent(country, k -> new ArrayList<>()).add(province);
    }

    public void removeBorder(String country, Province province) {
        borderProvinces.computeIfPresent(country, (key, provinces) -> {
            provinces.remove(province);
            return provinces.isEmpty() ? null : provinces;
        });
    }

    public boolean containsBorder(String country) {
        return borderProvinces.containsKey(country);
    }

    public boolean containsBorder(String country, Province province) {
        return borderProvinces.containsKey(country) && borderProvinces.get(country).contains(province);
    }

    public List<Province> getBorder(String country) {
        return borderProvinces.getOrDefault(country, new ArrayList<>());
    }


    public void addMajorCityBlock(Province province, Material material) {
        majorCityBlocks.put(province, material);
    }

    public void removeMajorCityBlock(Province province) {
        majorCityBlocks.remove(province);
    }

    public boolean containsMajorCityBlock(Province province) {
        return majorCityBlocks.containsKey(province);
    }

    public Material getMajorCityBlock(Province province) {
        return majorCityBlocks.get(province);
    }

    public void addCountryWar(String war) {
        countryWars.add(war);
    }

    public void removeCountryWar(String war) {
        countryWars.remove(war);
    }

    public boolean containsCountryWar(String war) {
        return countryWars.contains(war);
    }

    public Set<String> getCountryWars() {
        return new HashSet<>(countryWars);
    }

    public void addBorderWar(String war) {
        bordersWars.add(war);
    }

    public void removeBorderWar(String war) {
        bordersWars.remove(war);
    }

    public boolean containsBorderWar(String war) {
        return bordersWars.contains(war);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.add("occupies", gson.toJsonTree(occupies));
        json.add("cities", gson.toJsonTree(cities));
        json.add("occupiesOthersCores", gson.toJsonTree(occupiesOthersCores));
        JsonArray bordersProvinceArray = new JsonArray();
        borderProvinces.forEach((string, provinces) -> {
            JsonObject jsonObject = new JsonObject();
            JsonArray provinceArray = new JsonArray();
            provinces.forEach(province -> provinceArray.add(province.toJson()));
            jsonObject.add(string,provinceArray);
            bordersProvinceArray.add(jsonObject);
        });
        json.add("bordersProvince", bordersProvinceArray);
        JsonArray majorBlocks = new JsonArray();
        majorCityBlocks.forEach((province, material) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(material.name(),province.toJson());
            majorBlocks.add(jsonObject);
        });

        json.add("majorCityBlocks", majorBlocks);
        json.add("countryWars", gson.toJsonTree(countryWars));
        json.add("bordersWars", gson.toJsonTree(bordersWars));
        return json;
    }

}
