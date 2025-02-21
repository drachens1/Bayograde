package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.additional.Modifier;

public record WarGoalType(String name, Modifier modifier, int expires, int timeToMake) {
}
