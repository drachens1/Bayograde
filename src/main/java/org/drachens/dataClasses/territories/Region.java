package org.drachens.dataClasses.territories;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.ElectionTypes;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Province;

import java.util.ArrayList;
import java.util.List;

public class Region {
    private List<Country> countries = new ArrayList<>();
    private List<Province> provinces = new ArrayList<>();
    private final IdeologyTypes leadingIdeology;
    private final ElectionTypes leadingElectionType;
    private String name;

    public Region(String name, IdeologyTypes leadingIdeology, ElectionTypes leadingElectionType) {
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

    public ElectionTypes getLeadingElectionType() {
        return leadingElectionType;
    }

    public String getName() {
        return name;
    }
}
