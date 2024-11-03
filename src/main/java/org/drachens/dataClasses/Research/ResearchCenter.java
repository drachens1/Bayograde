package org.drachens.dataClasses.Research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.Provinces.Province;

import static org.drachens.Manager.defaults.ContinentalManagers.defaultsStorer;

public class ResearchCenter extends BuildTypes {
    private final Component cantAffordMsg = Component.text()
            .append(Component.text("You cannot afford the research center : 5 Production", NamedTextColor.RED))
            .build();

    private final Component cantAffordToUpgrade = Component.text()
            .append(Component.text("You cannot afford to upgrade this research center : 5 Production", NamedTextColor.RED))
            .build();

    private final Component maxCapacityReached = Component.text()
            .append(Component.text("Max capacity has been reached",NamedTextColor.RED))
            .build();
    public ResearchCenter() {
        super(new int[]{1}, Material.BROWN_DYE, "research_center");
    }
    private final Payments payments = new Payments(new Payment(defaultsStorer.currencies.getCurrencyType("production"),5f));

    @Override
    public void onBuild(Country country, Province province, Player p) {
        country.removePayments(payments);
        new Building(this, province);
    }

    @Override
    public boolean canBuild(Country country, Province province, Player p) {
        if (province.getOccupier()!=country)return false;
        if (province.getBuilding()!=null)return false;
        if (!country.canMinusCosts(payments)) {
            p.sendMessage(cantAffordMsg);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirementsToUpgrade(Building building, Country country, int add, Player p) {
        return false;
    }

    @Override
    public boolean requirementsToDestroy(Country country) {
        return false;
    }

    @Override
    protected void onCaptured(Country capturer, Building building) {

    }

    @Override
    protected void bombed(float dmg) {

    }

    @Override
    protected void onDestroyed(Building building) {

    }

    @Override
    protected void onUpgrade(int amount, Building building) {

    }
}
