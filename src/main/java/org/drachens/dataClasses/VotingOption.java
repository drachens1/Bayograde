package org.drachens.dataClasses;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.interfaces.MapGen;
import org.drachens.interfaces.Saveable;
import org.drachens.interfaces.War;

import java.util.HashMap;
import java.util.List;

@Builder
@Getter
public class VotingOption implements Saveable {
    private final int countries;
    private final int startingYear;
    private final int endYear;
    private final Long dayLength;
    private final War war;
    private final String name;
    private final MapGen mapGenerator;
    private final List<IdeologyTypes> ideologyTypes;
    private final HashMap<CurrencyTypes, Currencies> defaultCurrencies;
    private final InventoryEnum defaultInventory;
    private final TechTree tree;
    private final boolean instaBuild;
    private final boolean AIEnabled;
    private final boolean factionsEnabled;
    private final boolean researchEnabled;
    private final long speed;
    private final long progressionRate;

    public static VotingOptionBuilder create(int startingYear, int endYear, Long dayLength, String name) {
        return VotingOption.builder()
                .startingYear(startingYear)
                .endYear(endYear)
                .dayLength(dayLength)
                .name(name);
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
