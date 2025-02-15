package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.additional.Modifier;

public record WarGoalType(String name, Modifier modifier, float expires, float timeToMake) {
}
