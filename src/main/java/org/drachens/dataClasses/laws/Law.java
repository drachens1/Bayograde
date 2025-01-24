package org.drachens.dataClasses.laws;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.Modifier;

import java.util.function.Function;

public record Law(String identifier, Modifier modifier, Function<Country, Boolean> isAvailable) {
}