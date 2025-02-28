package org.drachens.dataClasses.Economics;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.animation.Animation;
import org.drachens.animation.AnimationType;
import org.drachens.dataClasses.Countdown;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.player_types.CPlayer;

import java.util.HashSet;
import java.util.function.Function;

import static org.drachens.util.ItemStackUtil.itemBuilder;

@Getter
public abstract class BuildTypes {
    protected final int[] lvlsModelData;
    private final Material material;
    private final BuildingEnum identifier;
    private final Function<Province, ItemStack> canItem;
    private final Function<Province, ItemStack> cantItem;
    private final AnimationType buildingAnimation
            = new Animation(500L, Material.BLUE_DYE, new int[]{1,2});
    private final ItemStack norm = itemBuilder(Material.BLUE_DYE,3);
    private boolean buildtimeEnabled = false;
    private int buildTime;
    private HashSet<Building> built = new HashSet<>();

    protected BuildTypes(int[] lvls, Material material, BuildingEnum identifier) {
        this.lvlsModelData = lvls;
        this.material = material;
        this.identifier = identifier;
        ItemStack i = itemBuilder(material,lvls[0]);
        canItem = integer -> i;
        cantItem = integer -> i;
    }

    protected BuildTypes(int[] lvls, Material material, BuildingEnum identifier, Function<Province, ItemStack> canItem, Function<Province, ItemStack> cantItem) {
        this.lvlsModelData = lvls;
        this.material = material;
        this.identifier = identifier;
        this.canItem = canItem;
        this.cantItem = cantItem;
    }

    protected void setBuildTime(int buildTime){
        this.buildTime=buildTime;
        buildtimeEnabled=true;
    }

    public void forceBuild(Country country, Province province, CPlayer p) {
        onBuild(country, province, p, 0f);
    }

    public boolean isBuilt(Building building){
        return built.contains(building);
    }

    public void build(Country country, Province province, CPlayer p) {
        if (canBuild(country, province, p)) {
            onBuild(country, province, p, 0f);
            if (buildtimeEnabled) buildingAnimation(country,province);
        }
    }

    public void build(Country country, Province province, CPlayer p, float yaw) {
        if (canBuild(country, province, p)) {
            onBuild(country, province, p, yaw);
            if (buildtimeEnabled) buildingAnimation(country,province);
        }
    }

    public void buildingAnimation(Country country, Province province){
        ItemDisplay anim = new ItemDisplay(itemBuilder(Material.BLUE_DYE,1),province.getPos().add(1,2,1), ItemDisplay.DisplayType.NONE, province.getInstance());
        anim.setScale(new Pos(2,2,2));
        country.addClientside(anim);
        buildingAnimation.start(anim,true);
        new Countdown(buildTime,()-> {
            country.removeClientside(anim);
            anim.dispose();
            built.add(province.getBuilding());
        }).start(anim.getInstance());
    }

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
            onUpgrade(amount, building, country);
        }
    }

    protected void onUpgrade(int amount, Building building, Country country) {}

    public int getLvl(int current) {
        return lvlsModelData[current];
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
