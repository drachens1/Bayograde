package org.drachens.temporary;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Scheduler;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.animation.Animation;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.dataClasses.territories.Province;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.Messages.sendMessage;


public class Factory extends BuildTypes {
    private final Payments produces;
    private final Payments payments = new Payments(new Payment(CurrencyEnum.production, 5f));
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
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    int[] constructionFrames = {2, 3, 4, 5};
    private final Animation constructionAnimation = new Animation(1000, Material.CYAN_DYE, constructionFrames);
    int[] smokeFrames = {6, 7, 8};
    private final Animation smokeAnimation = new Animation(1000, Material.CYAN_DYE, smokeFrames);

    public Factory() {
        super(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, Material.CYAN_DYE, BuildingEnum.factory);
        produces = new Payments(new Payment(CurrencyEnum.production, 1000f));
        materialLvls.put(Material.CYAN_GLAZED_TERRACOTTA, 1);
        materialLvls.put(Material.GREEN_GLAZED_TERRACOTTA, 2);
        materialLvls.put(Material.LIME_GLAZED_TERRACOTTA, 3);
        materialLvls.put(Material.YELLOW_GLAZED_TERRACOTTA, 4);
        materialLvls.put(Material.RAW_GOLD_BLOCK, 5);
        materialLvls.put(Material.GOLD_BLOCK, 6);
        materialLvls.put(Material.EMERALD_BLOCK, 7);
    }

    @Override
    public void onBuild(Country country, Province province, CPlayer p) {
        country.removePayments(payments);
        Building factory = new Building(this, province);
        ItemDisplay itemDisplay = factory.getItemDisplay();
        constructionAnimation.start(itemDisplay).onFinish(itemDisplay, () -> smokeAnimation.start(itemDisplay, true, 1000L));
    }

    @Override
    public boolean canBuild(Country country, Province province, CPlayer p) {
        if (province.getOccupier() != country&&country.isPuppet(province.getOccupier())) return false;
        if (province.getBuilding() != null) return false;
        if (!province.isCity()) return false;
        if (!country.canMinusCosts(payments)) {
            sendMessage(p,cantAffordMsg);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirementsToUpgrade(Building building, Country country, int add, CPlayer p) {
        if (building.getCountry() != country) return false;
        Province province = building.getProvince();
        if (!materialLvls.containsKey(province.getMaterial())) return false;
        Payments temp = new Payments(payments);
        temp.multiply(add);
        if (!country.canMinusCosts(temp)) {
            sendMessage(p,cantAffordToUpgrade);
            return false;
        }
        int maxLvl = materialLvls.get(province.getMaterial());
        maxLvl = Math.round(maxLvl * country.getBoost(BoostEnum.buildingSlotBoost));
        if (lvlsModelData.length<maxLvl)return false;
        if (!(building.getCurrentLvl() + add <= maxLvl)) {
            sendMessage(p,maxCapacityReached);
            return false;
        } else
            return true;
    }

    @Override
    protected void onCaptured(Country capturer, Building building) {
        building.getCountry().removeBuilding(building);
        capturer.addBuilding(building);
        building.setCountry(capturer);
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
        Country country = building.getCountry();
        Payments payments1 = new Payments(produces);
        payments1.multiply(country.getBoost(BoostEnum.production) + building.getCurrentLvl());
        createFloatingText(building, payments1.getMessages());
        return payments1;
    }

    public void createFloatingText(Building building, Component text) {
        Province province = building.getProvince();
        long initialDelay = new Random().nextLong(0, 200);
        scheduler.buildTask(() -> {
            TextDisplay textDisplay = new TextDisplay.create(province, text)
                    .setFollowPlayer(true)
                    .setLineWidth(40)
                    .withOffset()
                    .build();
            building.getCountry().addANotSavedTextDisplay(textDisplay);
            scheduler.buildTask(() -> {
                textDisplay.moveNoRotation(new Pos(0, 4, 0), 40, true);
                textDisplay.destroy(3995L);
            }).delay(initialDelay + new Random().nextLong(0, 200), ChronoUnit.MILLIS).schedule();
        }).delay(initialDelay, ChronoUnit.MILLIS).schedule();
    }
}
