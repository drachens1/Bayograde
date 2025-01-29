package org.drachens.dataClasses.Economics;

import org.drachens.player_types.CPlayer;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;

import java.util.HashSet;

public abstract class BuildTypes {
    protected final int[] lvlsModelData;
    private final Material material;
    private final BuildingEnum identifier;

    public BuildTypes(int[] lvls, Material material, BuildingEnum identifier) {
        this.lvlsModelData = lvls;
        this.material = material;
        this.identifier = identifier;
    }

    public void forceBuild(Country country, Province province, CPlayer p) {
        onBuild(country, province, p);
    }

    public void build(Country country, Province province, CPlayer p) {
        if (canBuild(country, province, p)) onBuild(country, province, p);
    }

    public void onBuild(Country country, Province province, CPlayer p) {

    }

    public boolean canBuild(Country country, Province province, CPlayer p) {
        return false;
    }

    public boolean requirementsToUpgrade(Building building, Country country, int add, CPlayer p) {
        return false;
    }

    public boolean requirementsToDestroy(Country country) {
        return false;
    }

    public void capture(Country capturer, Building building) {
        onCaptured(capturer, building);
    }

    protected void onCaptured(Country capturer, Building building) {

    }

    public void bomb(float dmg) {
        bombed(dmg);
    }

    protected void bombed(float dmg) {

    }

    public void destroy(Building building) {
        onDestroyed(building);
    }

    protected void onDestroyed(Building building) {

    }

    public void upgrade(int amount, Building building, Country country, CPlayer p) {
        if (requirementsToUpgrade(building, country, amount, p)) {
            onUpgrade(amount, building);
        }
    }

    protected void onUpgrade(int amount, Building building) {

    }

    public int getLvl(int current) {
        return lvlsModelData[current];
    }

    public Material getMaterial() {
        return material;
    }

    public BuildingEnum getIdentifier() {
        return identifier;
    }

    public HashSet<String> getSynonyms() {
        return identifier.getSynonyms();
    }
}
