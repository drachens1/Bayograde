package org.drachens.dataClasses.Economics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;

import java.util.HashSet;

import static org.drachens.util.ItemStackUtil.itemBuilder;

@Getter
@Setter
public class Building implements Saveable {
    private final HashSet<String> synonyms;
    private final BuildingEnum buildType;
    private final Province province;
    private final ItemDisplay itemDisplay;
    private Country country;
    private int current = 1;

    public Building(BuildTypes buildTypes, Province province) {
        this.buildType = buildTypes.getIdentifier();
        this.synonyms = buildTypes.getSynonyms();
        this.province = province;
        this.country = province.getOccupier();
        this.itemDisplay = new ItemDisplay(itemBuilder(buildTypes.getMaterial(), buildTypes.getLvl(0)), province, ItemDisplay.DisplayType.GROUND);
        itemDisplay.setPosWithOffset(province);
        country.addBuilding(this);
        province.addBuilding(this);
    }
    public void capture(Country capturer) {
        buildType.getBuildTypes().capture(capturer, this);
    }

    public void upgrade(int amount, Country country, CPlayer p) {
        buildType.getBuildTypes().upgrade(amount, this, country, p);
    }

    public void bomb(float bomb) {
        buildType.getBuildTypes().bomb(bomb);
    }

    public int getCurrentLvl() {
        return current;
    }

    public BuildingEnum getBuildTypes() {
        return buildType;
    }

    public boolean hasSynonym(BuildingEnum buildingEnum) {
        return synonyms.contains(buildingEnum.name());
    }

    public void delete() {
        this.itemDisplay.dispose();
        this.country.removeBuilding(this);
        this.province.removeBuilding();
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("province", gson.toJsonTree(province));
        jsonObject.add("lvl",new JsonPrimitive(current));
        return jsonObject;
    }
}
