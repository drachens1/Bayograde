package org.drachens.generalGame.clicks;

import com.google.gson.JsonElement;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.interfaces.Saveable;
import org.drachens.interfaces.ai.AI;
import org.drachens.interfaces.ai.AIManager;

import java.util.HashMap;

public class ClicksAI implements AIManager, Saveable {
    private final VotingWinner votingWinner;
    private final HashMap<Country, AI> ais = new HashMap<>();

    public ClicksAI(VotingWinner votingWinner) {
        this.votingWinner = votingWinner;
    }

    @Override
    public VotingWinner getIdentifier() {
        return votingWinner;
    }

    @Override
    public AI createAIForCountry(Country country) {
        AI ai = new UnifiedClicksAI(country,(ClickWarSystem) votingWinner.getVotingOption().getWar());
        ais.put(country, ai);
        return ai;
    }

    @Override
    public void tick(Instance instance) {
        ais.forEach((country, ai) -> {
            ai.tick();
        });
    }

    @Override
    public void fasterTick(Instance instance) {
        ais.forEach((country, ai) -> {
            if (country.getInfo().getPlayers().isEmpty()) ai.fasterTick();
        });
    }

    @Override
    public void removeAi(Country country) {
        ais.remove(country);
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
