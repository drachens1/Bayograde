package org.drachens.interfaces;

import org.drachens.dataClasses.Countries.Country;

public abstract class AICompetitor extends AI {
    private Country currentCountry;

    @Override
    public int selectAction(int state) {
        return 0;
    }

    @Override
    public void updateKnowledge(int previousState, int action, double reward, int newState) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void evaluatePerformance() {

    }
}
