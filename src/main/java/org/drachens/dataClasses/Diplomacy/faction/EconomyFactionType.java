package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;
import org.drachens.interfaces.MapGen;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class EconomyFactionType implements FactionType  {
    private Modifier factionModifier;
    private Factions factions;
    private final Component name = compBuild("Economy",NamedTextColor.GOLD);

    @Override
    public Component getName() {
        return name;
    }
    @Override
    public void setFactions(Factions factions){
        this.factions = factions;
        this.factionModifier = new Modifier.create(compBuild(factions.getName(), NamedTextColor.GREEN))
                .setDescription(compBuild("The bonuses from the economic faction",NamedTextColor.GRAY))
                .build();
        factions.getMembers().forEach((country -> country.addModifier(factionModifier)));
        updateFactionModifier();
    }

    @Override
    public void countryJoins(Country country) {
        country.addModifier(factionModifier);
    }

    @Override
    public void countryLeaves(Country country) {
        country.removeModifier(factionModifier);
    }

    public Modifier getFactionModifier(){
        return factionModifier;
    }
    public void updateFactionModifier(){
        float totalDistance = 0f;
        int numOfCountries = 0;
        Instance instance = factions.getCreator().getCapital().getInstance();
        for (Country country : factions.getMembers()){
            List<Country> countries = new ArrayList<>(factions.getMembers());
            countries.remove(country);
            numOfCountries++;
            for (Country country1 : countries){
                totalDistance += (float) Math.abs(country.getCapital().distance(country1.getCapital()));
            }
        }
        float boost = calculateBoost(totalDistance,numOfCountries,instance);
        factionModifier.setProductionBoost(boost);
    }
    private float calculateBoost(float distance, int numOfCountries, Instance instance){
        if (numOfCountries==1)return 0f;
        float boost = distance/numOfCountries;
        MapGen mapGenerator = ContinentalManagers.world(instance).votingManager().getWinner().getMapGenerator();
        float mapSize = (mapGenerator.getSizeX() + mapGenerator.getSizeY())/2f;
        return boost * mapSize;
    }
}
