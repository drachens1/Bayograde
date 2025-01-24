package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.Modifier;

public enum WarGoalTypeEnum {
    justified(new WarGoalType("justified", new Modifier.create(null, "justified").setDisplay(false).build(), 30, 120)),
    partially_justified(new WarGoalType("partially-justified", new Modifier.create(null, "partially-justified").setDisplay(false).addBoost(BoostEnum.stabilityBase, -5f).build(), 30, 85)),
    surprise(new WarGoalType("surprise", new Modifier.create(null, "surprise").setDisplay(false).addBoost(BoostEnum.stabilityBase, -55f).addBoost(BoostEnum.relations, -50f).build(), 30f, 0f));

    private final WarGoalType warGoalType;

    WarGoalTypeEnum(WarGoalType warGoalType) {
        this.warGoalType = warGoalType;
    }

    public WarGoalType getWarGoalType() {
        return warGoalType;
    }
}
