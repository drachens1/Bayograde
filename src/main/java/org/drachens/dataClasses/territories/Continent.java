package org.drachens.dataClasses.territories;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;

import java.util.ArrayList;
import java.util.List;
// Continent dataclass is only used during map generation
public class Continent {
    private List<Country> countries = new ArrayList<>();
    private List<Province> provinces = new ArrayList<>();

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public void addCountry(Country country){
        this.countries.add(country);
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public void addProvince(Province province){
        this.provinces.add(province);
    }
}
