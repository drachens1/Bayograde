package org.drachens.interfaces;

public abstract class AI {
    public abstract int selectAction(int state);

    public abstract void updateKnowledge(int previousState, int action, double reward, int newState);

    public abstract void reset();

    public abstract void evaluatePerformance();
}
