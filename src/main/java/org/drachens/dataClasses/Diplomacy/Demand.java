package org.drachens.dataClasses.Diplomacy;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;

import java.util.List;

public class Demand {
    private List<Province> demandedProvinces;
    private Country fromCountry;
    private Country toCountry;
    public Demand(List<Province> demandedProvinces, Country fromCountry, Country toCountry) {
        this.demandedProvinces = demandedProvinces;
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
    }
    public Country getFromCountry() {
        return fromCountry;
    }
    public Country getToCountry() {
        return toCountry;
    }
    public List<Province> getDemandedProvinces() {
        return demandedProvinces;
    }
}
