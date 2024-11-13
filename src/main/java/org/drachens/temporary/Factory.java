package org.drachens.temporary;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.DefaultsStorer;
import org.drachens.animation.Animation;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import java.util.HashMap;

import static org.drachens.Manager.defaults.ContinentalManagers.defaultsStorer;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Factory extends BuildTypes {
    private final Payments produces;
    int[] constructionFrames = {2, 3, 4, 5};
    int[] smokeFrames = {6, 7, 8};
    private final Animation constructionAnimation = new Animation(1000, Material.CYAN_DYE, constructionFrames);
    private final Animation smokeAnimation = new Animation(1000, Material.CYAN_DYE, smokeFrames);
    private final Payments payments = new Payments(new Payment(defaultsStorer.currencies.getCurrencyType("production"), 5f));
    private final Component cantAffordMsg = Component.text()
            .append(Component.text("You cannot afford the factory : 5 Production", NamedTextColor.RED))
            .build();

    private final Component cantAffordToUpgrade = Component.text()
            .append(Component.text("You cannot afford to upgrade this factory : 5 Production", NamedTextColor.RED))
            .build();

    private final Component maxCapacityReached = Component.text()
            .append(Component.text("Max capacity has been reached", NamedTextColor.RED))
            .build();

    private final HashMap<Material, Integer> materialLvls = new HashMap<>();

    public Factory() {
        super(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, Material.CYAN_DYE, "factory");
        DefaultsStorer defaultsStorer = ContinentalManagers.defaultsStorer;
        CurrencyTypes production = defaultsStorer.currencies.getCurrencyType("production");
        produces = new Payments(new Payment(production, 2f));
        System.out.println(produces.getPayments().getFirst().getAmount() + " Factory");
        materialLvls.put(Material.CYAN_GLAZED_TERRACOTTA, 1);
        materialLvls.put(Material.GREEN_GLAZED_TERRACOTTA, 2);
        materialLvls.put(Material.LIME_GLAZED_TERRACOTTA, 3);
        materialLvls.put(Material.YELLOW_GLAZED_TERRACOTTA, 4);
        materialLvls.put(Material.RAW_GOLD_BLOCK, 5);
        materialLvls.put(Material.GOLD_BLOCK, 6);
        materialLvls.put(Material.EMERALD_BLOCK, 7);
    }

    @Override
    public void onBuild(Country country, Province province, Player p) {
        country.removePayments(payments);
        Building factory = new Building(this, province);
        ItemDisplay itemDisplay = factory.getItemDisplay();
        constructionAnimation.start(itemDisplay).onFinish(itemDisplay, () -> smokeAnimation.start(itemDisplay, true, 1000L));
    }

    @Override
    public boolean canBuild(Country country, Province province, Player p) {
        if (province.getOccupier() != country) return false;
        if (province.getBuilding() != null) return false;
        if (!province.isCity()) return false;
        if (!country.canMinusCosts(payments)) {
            p.sendMessage(cantAffordMsg);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirementsToUpgrade(Building building, Country country, int add, Player p) {
        if (building.getCountry() != country) return false;
        Province province = building.getProvince();
        if (!materialLvls.containsKey(province.getMaterial())) return false;
        Payments temp = new Payments(payments);
        temp.multiply(add);
        if (!country.canMinusCosts(temp)) {
            p.sendMessage(cantAffordToUpgrade);
            return false;
        }
        int maxLvl = materialLvls.get(province.getMaterial());
        maxLvl = Math.round(maxLvl * country.getMaxBuildingSlotBoost());
        System.out.println(maxLvl + " = " + materialLvls.get(province.getMaterial()) + " * " + country.getMaxBuildingSlotBoost());
        if (!(building.getCurrentLvl() + add <= maxLvl)) {
            p.sendMessage(maxCapacityReached);
            return false;
        } else
            return true;
    }

    @Override
    public boolean requirementsToDestroy(Country country) {
        return false;
    }

    @Override
    protected void onCaptured(Country capturer, Building building) {
        building.getCountry().removeBuilding(building);
        capturer.addBuilding(building);
        building.setCountry(capturer);
    }

    @Override
    protected void bombed(float dmg) {
    }

    @Override
    protected void onDestroyed(Building building) {
        building.delete();
    }

    @Override
    protected void onUpgrade(int amount, Building building) {
        int num = building.getCurrentLvl() + amount;
        ItemDisplay itemDisplay = building.getItemDisplay();
        if (num <= 0) {
            building.delete();
        }
        building.setCurrent(num);
        itemDisplay.setItem(itemBuilder(getMaterial(), getLvl(num)));
    }

    public Payments generate(Building building) {
        Payments payments1 = new Payments(produces);
        payments1.multiply(building.getCurrentLvl());
        return payments1;
    }
    //Make a cool text display going up when it generates
}
