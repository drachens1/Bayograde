package org.drachens.interfaces.ai;

import lombok.Getter;
import lombok.Setter;
import org.drachens.generalGame.clicks.UnifiedClicksAI;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class QLearning {
    private HashMap<String, HashMap<UnifiedClicksAI.Action.ActionType, Double>> qTable = new HashMap<>();

    public UnifiedClicksAI.Action.ActionType chooseAction(String state) {
        double explorationRate = 0.2;
        if (Math.random() < explorationRate || !qTable.containsKey(state)) {
            return UnifiedClicksAI.Action.ActionType.values()[(int) (Math.random() * UnifiedClicksAI.Action.ActionType.values().length)];
        }
        return qTable.get(state).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseGet(() -> UnifiedClicksAI.Action.ActionType.values()[(int) (Math.random() * UnifiedClicksAI.Action.ActionType.values().length)]);
    }

    public void updateQValue(String state, UnifiedClicksAI.Action.ActionType action, double reward, String nextState) {
        qTable.putIfAbsent(state, new HashMap<>());
        HashMap<UnifiedClicksAI.Action.ActionType, Double> actions = qTable.get(state);
        double oldQ = actions.getOrDefault(action, 0.0);
        double maxFutureQ = qTable.getOrDefault(nextState, new HashMap<>()).values().stream().mapToDouble(v -> v).max().orElse(0.0);

        double learningRate = 0.1;
        double discountFactor = 0.9;
        double newQ = oldQ + learningRate * (reward + discountFactor * maxFutureQ - oldQ);
        actions.put(action, newQ);
    }
}
