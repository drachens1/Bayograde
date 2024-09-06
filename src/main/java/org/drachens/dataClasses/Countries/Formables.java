package org.drachens.dataClasses.Countries;

import org.drachens.dataClasses.Provinces.Province;

import java.util.List;

public class Formables {
    private String name;
    private List<Country> canForm;
    private List<Province> requiredProvinces;
    public Formables(String name, List<Country> canForm, List<Province> requiredProvinces){

    }
    public String getName(){
        return name;
    }
    public List<Country> getCanForm(){
        return canForm;
    }
    public List<Province> getRequiredProvinces(){
        return requiredProvinces;
    }
}
