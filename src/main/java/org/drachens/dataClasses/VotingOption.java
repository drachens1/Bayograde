package org.drachens.dataClasses;

import org.drachens.dataClasses.Countries.ElectionTypes;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.interfaces.MapGen;
import org.drachens.interfaces.War;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VotingOption {
    int countries;
    int startingYear;
    int endYear;
    Long dayLength;
    War war;
    String name;
    MapGen mapGenerator;
    List<IdeologyTypes> ideologyTypes;
    List<ElectionTypes> electionTypes;
    HashMap<CurrencyTypes, Currencies> defaultCurrencies;
    TechTree tree;

    VotingOption(create c) {
        this.startingYear = c.startingYear;
        this.endYear = c.endYear;
        this.dayLength = c.dayLength;
        this.name = c.name;
        if (c.mapGenerator != null) mapGenerator = c.mapGenerator;
        if (c.war != null) war = c.war;
        if (c.ideologyTypes != null) ideologyTypes = c.ideologyTypes;
        if (c.defaultCurrencies != null) defaultCurrencies = c.defaultCurrencies;
        if (c.tree!=null) tree=c.tree;
        countries = c.countries;
        if (c.electionTypes != null) electionTypes = c.electionTypes;
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

    public void setMapGenerator(MapGen mapGenerator) {
        this.mapGenerator = mapGenerator;
    }

    public int getCountries() {
        return countries;
    }

    public HashMap<CurrencyTypes, Currencies> getDefaultCurrencies() {
        return defaultCurrencies;
    }

    public List<ElectionTypes> getElectionTypes() {
        return electionTypes;
    }

    public List<IdeologyTypes> getIdeologyTypes() {
        return ideologyTypes;
    }

    public TechTree getTree(){
        return tree;
    }

    public static class create {
        int startingYear;
        int endYear;
        int countries = 30;
        Long dayLength;
        War war;
        String name;
        MapGen mapGenerator;
        List<ElectionTypes> electionTypes;
        List<IdeologyTypes> ideologyTypes = new ArrayList<>();
        HashMap<CurrencyTypes, Currencies> defaultCurrencies = new HashMap<>();
        TechTree tree;

        public create(int startingYear, int endYear, Long dayLength, String name) {
            this.startingYear = startingYear;
            this.endYear = endYear;
            this.dayLength = dayLength;
            this.name = name;
        }

        public create setWar(War war) {
            this.war = war;
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

        public create setElections(List<ElectionTypes> elections) {
            this.electionTypes = elections;
            return this;
        }

        public create setTechTree(TechTree tree){
            this.tree=tree;
            return this;
        }

        public VotingOption build() {
            return new VotingOption(this);
        }
    }
}
