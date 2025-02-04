package org.drachens.dataClasses.Economics;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.player_types.CPlayer;

import java.util.HashSet;
import java.util.function.Function;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public abstract class BuildTypes {
    protected final int[] lvlsModelData;
    private final Material material;
    private final BuildingEnum identifier;
    private final Function<Province, ItemStack> canItem;
    private final Function<Province, ItemStack> cantItem;

    public BuildTypes(int[] lvls, Material material, BuildingEnum identifier) {
        this.lvlsModelData = lvls;
        this.material = material;
        this.identifier = identifier;
        ItemStack i = itemBuilder(material,lvls[0]);
        canItem = integer -> i;
        cantItem = integer -> i;
    }

    public BuildTypes(int[] lvls, Material material, BuildingEnum identifier, Function<Province, ItemStack> canItem, Function<Province, ItemStack> cantItem) {
        this.lvlsModelData = lvls;
        this.material = material;
        this.identifier = identifier;
        this.canItem = canItem;
        this.cantItem = cantItem;
    }

    public void forceBuild(Country country, Province province, CPlayer p) {
        onBuild(country, province, p);
    }

    public void build(Country country, Province province, CPlayer p) {
        if (canBuild(country, province, p)) onBuild(country, province, p);
    }

    public void build(Country country, Province province, CPlayer p, float yaw) {
        if (canBuild(country, province, p)) onBuild(country, province, p, yaw);
    }

    public void onBuild(Country country, Province province, CPlayer p) {}

    public void onBuild(Country country, Province province, CPlayer p, float yaw) {}

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

    protected void onDestroyed(Building building) {}

    public void upgrade(int amount, Building building, Country country, CPlayer p) {
        if (requirementsToUpgrade(building, country, amount, p)) {
            onUpgrade(amount, building);
        }
    }

    protected void onUpgrade(int amount, Building building) {}

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

    public ItemStack getCanItem(Province province){
        return canItem.apply(province);
    }

    public ItemStack getCantItem(Province province){
        return cantItem.apply(province);
    }
}
