package org.drachens.dataClasses.territories;

import org.drachens.Manager.defaults.enums.ElectionsEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.IdeologyTypes;

import java.util.ArrayList;
import java.util.List;

public class Region {
    private final IdeologyTypes leadingIdeology;
    private final ElectionsEnum leadingElectionType;
    private List<Country> countries = new ArrayList<>();
    private List<Province> provinces = new ArrayList<>();
    private final String name;

    public Region(String name, IdeologyTypes leadingIdeology, ElectionsEnum leadingElectionType) {
        this.leadingIdeology = leadingIdeology;
        this.leadingElectionType = leadingElectionType;
        this.name = name;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public void addCountry(Country country) {
        this.countries.add(country);
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public void addProvince(Province province) {
        this.provinces.add(province);
    }

    public IdeologyTypes getLeadingIdeology() {
        return leadingIdeology;
    }

    public ElectionsEnum getLeadingElectionType() {
        return leadingElectionType;
    }

    public String getName() {
        return name;
    }
}
