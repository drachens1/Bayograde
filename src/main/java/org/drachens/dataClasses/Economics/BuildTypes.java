package org.drachens.dataClasses.Economics;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;

public abstract class BuildTypes {
    private final int[] lvlsModelData;
    private final Material material;
    private final String identifier;

    public BuildTypes(int[] lvls, Material material, String identifier) {
        this.lvlsModelData = lvls;
        this.material = material;
        this.identifier = identifier;
        ContinentalManagers.defaultsStorer.buildingTypes.register(this);
    }

    public void build(Country country, Province province, Player p) {
        if (canBuild(country, province, p)) onBuild(country, province, p);
    }

    public abstract void onBuild(Country country, Province province, Player p);

    public abstract boolean canBuild(Country country, Province province, Player p);

    public abstract boolean requirementsToUpgrade(Building building, Country country, int add, Player p);

    public abstract boolean requirementsToDestroy(Country country);

    public void capture(Country capturer, Building building) {
        onCaptured(capturer, building);
    }

    protected abstract void onCaptured(Country capturer, Building building);

    public void bomb(float dmg) {
        bombed(dmg);
    }

    protected abstract void bombed(float dmg);

    public void destroy(Building building) {
        onDestroyed(building);
    }

    protected abstract void onDestroyed(Building building);

    public void upgrade(int amount, Building building, Country country, Player p) {
        if (requirementsToUpgrade(building, country, amount, p)) {
            onUpgrade(amount, building);
        }
    }

    protected abstract void onUpgrade(int amount, Building building);

    public int getLvl(int current) {
        return lvlsModelData[current];
    }

    public Material getMaterial() {
        return material;
    }

    public String getIdentifier() {
        return identifier;
    }
}
