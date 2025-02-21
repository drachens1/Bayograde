package org.drachens.generalGame.troops;

import com.google.gson.JsonElement;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.events.NewDay;
import org.drachens.generalGame.factory.Factory;
import org.drachens.interfaces.AI;
import org.drachens.interfaces.AIManager;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TroopAI implements AIManager, Saveable {
    private final HashMap<Country, AI> ais = new HashMap<>();
    private final VotingWinner votingWinner;

    public TroopAI(VotingWinner votingWinner) {
        this.votingWinner = votingWinner;
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e -> tick(e.world()));
    }

    @Override
    public VotingWinner getIdentifier() {
        return votingWinner;
    }

    @Override
    public AI createAIForCountry(Country country) {
        AI ai = new FactorySpammer(country);
        ais.put(country, ai);
        return ai;
    }

    @Override
    public void tick(Instance instance) {
        ais.forEach((country, ai) -> ai.tick());
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    static class FactorySpammer extends AI {
        private final Factory factory = (Factory) BuildingEnum.factory.getBuildTypes();
        private final Country country;
        private final Random r = new Random();

        public FactorySpammer(Country country) {
            this.country = country;
        }

        @Override
        public void tick() {
            buildFactory(new ArrayList<>(country.getMilitary().getCities()));
        }

        private void buildFactory(List<Province> cities) {
            while (true) {
                if (cities.isEmpty()) {
                    return;
                }
                final Province province = cities.get(this.r.nextInt(cities.size()));
                if (null != province.getBuilding()) {
                    if (this.factory.requirementsToUpgrade(province.getBuilding(), this.country, 1, null))
                        this.factory.upgrade(1, province.getBuilding(), this.country, null);
                } else if (this.factory.canBuild(this.country, province, null))
                    this.factory.forceBuild(this.country, province, null);
                else {
                    cities.remove(province);
                    continue;
                }

                return;
            }
        }
    }
}
