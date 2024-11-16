package org.drachens.dataClasses.Countries;

import org.drachens.dataClasses.Province;

import java.util.List;

public class Formables {
    private final String name;
    private final List<Country> canForm;
    private final List<Province> requiredProvinces;

    public Formables(String name, List<Country> canForm, List<Province> requiredProvinces) {
        this.name = name;
        this.canForm = canForm;
        this.requiredProvinces = requiredProvinces;
    }

    public String getName() {
        return name;
    }

    public List<Country> getCanForm() {
        return canForm;
    }

    public List<Province> getRequiredProvinces() {
        return requiredProvinces;
    }
}
