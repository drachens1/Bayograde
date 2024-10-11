package org.drachens.interfaces.Voting;

import org.drachens.dataClasses.Countries.ElectionTypes;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.interfaces.MapGenerator;
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
    MapGenerator mapGenerator;
    List<IdeologyTypes> ideologyTypes;
    List<ElectionTypes> electionTypes;
    HashMap<CurrencyTypes, Currencies> defaultCurrencies;
    VotingOption(create c){
        this.startingYear = c.startingYear;
        this.endYear = c.endYear;
        this.dayLength = c.dayLength;
        this.name = c.name;
        if (c.mapGenerator!=null) mapGenerator=c.mapGenerator;
        if (c.war!=null) war=c.war;
        if (c.ideologyTypes!=null) ideologyTypes = c.ideologyTypes;
        if (c.defaultCurrencies!=null) defaultCurrencies = c.defaultCurrencies;
        countries = c.countries;
        if (c.electionTypes!=null) electionTypes  = c.electionTypes;
    }
    public String getName(){
        return name;
    }
    public int getStartingYear(){
        return startingYear;
    }
    public int getEndYear(){
        return endYear;
    }
    public Long getDayLength(){
        return dayLength;
    }
    public War getWar(){
        return war;
    }
    public void setMapGenerator(MapGenerator mapGenerator){
        this.mapGenerator = mapGenerator;
    }
    public MapGenerator getMapGenerator(){
        return mapGenerator;
    }
    public int getCountries(){
        return countries;
    }
    public HashMap<CurrencyTypes, Currencies> getDefaultCurrencies(){
        return defaultCurrencies;
    }
    public List<ElectionTypes> getElectionTypes() {
        return electionTypes;
    }
    public List<IdeologyTypes> getIdeologyTypes(){
        return ideologyTypes;
    }
    public static class create {
        int startingYear;
        int endYear;
        int countries = 30;
        Long dayLength;
        War war;
        String name;
        MapGenerator mapGenerator;
        List<ElectionTypes> electionTypes;
        List<IdeologyTypes> ideologyTypes = new ArrayList<>();
        HashMap<CurrencyTypes, Currencies> defaultCurrencies = new HashMap<>();
        public create(int startingYear, int endYear, Long dayLength, String name){
            this.startingYear = startingYear;
            this.endYear = endYear;
            this.dayLength = dayLength;
            this.name = name;
        }
        public create setWar(War war){
            this.war = war;
            return this;
        }
        public create setMapGenerator(MapGenerator mapGenerator){
            this.mapGenerator = mapGenerator;
            return this;
        }
        public create setIdeologyTypes(List<IdeologyTypes> ideologyTypes){
            this.ideologyTypes = ideologyTypes;
            return this;
        }
        public create setDefaultCurrencies(HashMap<CurrencyTypes, Currencies> defaultCurrencies){
            this.defaultCurrencies = defaultCurrencies;
            return this;
        }
        public create setCountries(int countries){
            this.countries = countries;
            return this;
        }
        public create setElections(List<ElectionTypes> elections){
            this.electionTypes = elections;
            return this;
        }
        public VotingOption build(){
            return new VotingOption(this);
        }
    }
}
