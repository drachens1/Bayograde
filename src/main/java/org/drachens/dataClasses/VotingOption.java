package org.drachens.dataClasses;

import com.google.gson.JsonElement;
import org.drachens.Manager.defaults.enums.ElectionsEnum;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.interfaces.MapGen;
import org.drachens.interfaces.Saveable;
import org.drachens.interfaces.War;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VotingOption implements Saveable {
    private final int countries;
    private final int startingYear;
    private final int endYear;
    private final Long dayLength;
    private final War war;
    private final String name;
    private final MapGen mapGenerator;
    private final List<IdeologyTypes> ideologyTypes;
    private final List<ElectionsEnum> electionTypes;
    private final HashMap<CurrencyTypes, Currencies> defaultCurrencies;
    private final InventoryEnum defaultInventory;
    private final TechTree tree;
    private final boolean instaBuild;
    private final boolean AIEnabled;
    private final boolean factionsEnabled;
    private final boolean researchEnabled;
    private final long speed;
    private final long progressionRate;

    VotingOption(create c) {
        startingYear = c.startingYear;
        endYear = c.endYear;
        dayLength = c.dayLength;
        name = c.name;
        mapGenerator = c.mapGenerator;
        war = c.war;
        ideologyTypes = c.ideologyTypes;
        defaultCurrencies = c.defaultCurrencies;
        tree = c.tree;
        countries = c.countries;
        electionTypes = c.electionTypes;
        defaultInventory = c.defaultInventory;
        instaBuild = c.instaBuild;
        AIEnabled = c.AIEnabled;
        factionsEnabled = c.factionsEnabled;
        researchEnabled = c.researchEnabled;
        speed = c.speed;
        progressionRate = c.progressionRate;
    }

    public String getName() {
        return name;
    }

    public int getStartingYear() {
        return startingYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public Long getDayLength() {
        return dayLength;
    }

    public War getWar() {
        return war;
    }

    public MapGen getMapGenerator() {
        return mapGenerator;
    }

    public int getCountries() {
        return countries;
    }

    public HashMap<CurrencyTypes, Currencies> getDefaultCurrencies() {
        return defaultCurrencies;
    }

    public List<ElectionsEnum> getElectionTypes() {
        return electionTypes;
    }

    public List<IdeologyTypes> getIdeologyTypes() {
        return ideologyTypes;
    }

    public TechTree getTree() {
        return tree;
    }

    public InventoryEnum getDefaultInventory() {
        return defaultInventory;
    }

    public boolean isInstaBuild() {
        return instaBuild;
    }

    public boolean isAIEnabled() {
        return AIEnabled;
    }

    public boolean isFactionsEnabled() {
        return factionsEnabled;
    }

    public boolean isResearchEnabled() {
        return researchEnabled;
    }

    public long getSpeed() {
        return speed;
    }

    public long getProgressionRate() {
        return progressionRate;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    public static class create {
        private final int startingYear;
        private final int endYear;
        private int countries = 30;
        private final Long dayLength;
        private War war;
        private final String name;
        private MapGen mapGenerator;
        private List<ElectionsEnum> electionTypes;
        private List<IdeologyTypes> ideologyTypes = new ArrayList<>();
        private HashMap<CurrencyTypes, Currencies> defaultCurrencies = new HashMap<>();
        private TechTree tree;
        private InventoryEnum defaultInventory;
        private boolean instaBuild = false;
        private boolean AIEnabled = true;
        private boolean factionsEnabled = true;
        private boolean researchEnabled = true;
        private long speed = 1L;
        private long progressionRate = 1L;

        public create(int startingYear, int endYear, Long dayLength, String name) {
            this.startingYear = startingYear;
            this.endYear = endYear;
            this.dayLength = dayLength;
            this.name = name;
        }

        public create setInstaBuild(boolean instaBuild) {
            this.instaBuild = instaBuild;
            return this;
        }

        public create setAIEnabled(boolean AIEnabled) {
            this.AIEnabled = AIEnabled;
            return this;
        }

        public create setFactionsEnabled(boolean factionsEnabled) {
            this.factionsEnabled = factionsEnabled;
            return this;
        }

        public create setResearchEnabled(boolean researchEnabled) {
            this.researchEnabled = researchEnabled;
            return this;
        }

        public create setSpeed(long speed) {
            this.speed = speed;
            return this;
        }

        public create setProgressionRate(long progressionRate) {
            this.progressionRate = progressionRate;
            return this;
        }

        public create setWar(War war) {
            this.war = war;
            return this;
        }

        public create setDefaultInventory(InventoryEnum hotbarInventory) {
            this.defaultInventory = hotbarInventory;
            return this;
        }

        public create setMapGenerator(MapGen mapGenerator) {
            this.mapGenerator = mapGenerator;
            return this;
        }

        public create setIdeologyTypes(List<IdeologyTypes> ideologyTypes) {
            this.ideologyTypes = ideologyTypes;
            return this;
        }

        public create setDefaultCurrencies(HashMap<CurrencyTypes, Currencies> defaultCurrencies) {
            this.defaultCurrencies = defaultCurrencies;
            return this;
        }

        public create setCountries(int countries) {
            this.countries = countries;
            return this;
        }

        public create setElections(List<ElectionsEnum> elections) {
            this.electionTypes = elections;
            return this;
        }

        public create setTechTree(TechTree tree) {
            this.tree = tree;
            return this;
        }

        public VotingOption build() {
            return new VotingOption(this);
        }
    }
}
