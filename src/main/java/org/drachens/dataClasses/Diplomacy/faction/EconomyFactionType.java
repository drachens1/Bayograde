package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.BoostEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;
import org.drachens.interfaces.MapGen;

import java.util.ArrayList;
import java.util.List;

public class EconomyFactionType extends Factions {
    private final Component name = Component.text("Economy", NamedTextColor.GOLD);

    public EconomyFactionType(Country creator, String name) {
        super(creator, name, new Modifier.create(Component.text(name, NamedTextColor.GREEN))
                .setDescription(Component.text("The bonuses from the economic faction", NamedTextColor.GRAY))
                .build());
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public void addMember(Country country) {
        updateFactionModifier();
    }

    @Override
    public void removeMember(Country country) {
        updateFactionModifier();
    }

    public void updateFactionModifier() {
        float totalDistance = 0f;
        int numOfCountries = 0;
        Instance instance = getLeader().getCapital().getInstance();
        for (Country country : getMembers()) {
            List<Country> countries = new ArrayList<>(getMembers());
            countries.remove(country);
            numOfCountries++;
            for (Country country1 : countries) {
                totalDistance += (float) Math.abs(country.getCapital().distance(country1.getCapital()));
            }
        }
        float boost = calculateBoost(totalDistance, numOfCountries, instance);
        getModifier().setBoost(BoostEnum.production,boost);
    }

    private float calculateBoost(float distance, int numOfCountries, Instance instance) {
        if (numOfCountries == 1) return 0f;
        float boost = distance / numOfCountries;
        MapGen mapGenerator = ContinentalManagers.world(instance).dataStorer().votingOption.getMapGenerator();
        float mapSize = (mapGenerator.getSizeX() + mapGenerator.getSizeY()) / 2f;
        return boost * mapSize;
    }
}
