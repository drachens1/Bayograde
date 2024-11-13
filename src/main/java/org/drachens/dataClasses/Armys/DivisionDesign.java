package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.interfaces.DivisionStatsCalculator;

import java.util.HashMap;

public class DivisionDesign {
    private final Country country;
    private HashMap<Integer, DivisionType> design;
    private float hp;
    private float atk;
    private float def;
    private float speed;
    private HashMap<CurrencyTypes, Payment> paymentList;
    private String name;

    public DivisionDesign(String name, HashMap<Integer, DivisionType> design, DivisionStatsCalculator divisionStatsCalculator, Country country) {
        this.design = design;
        this.paymentList = new HashMap<>();
        this.hp = 1f;
        this.atk = 1f;
        this.def = 1f;
        this.speed = 1f;
        this.country = country;
        this.name = name;
    }

    public DivisionDesign(DivisionDesign design) {
        this.design = design.design;
        this.atk = design.atk;
        this.def = design.def;
        this.speed = design.speed;
        this.paymentList = design.paymentList;
        this.country = design.country;
        this.name = design.name;
    }

    public HashMap<Integer, DivisionType> getDesign() {
        return design;
    }

    public void setDesign(HashMap<Integer, DivisionType> design) {
        this.design = design;
    }

    public void addDesign(int slot, DivisionType divisionType) {
        this.design.put(slot, divisionType);
    }

    public DivisionType getDivisionType(int slot) {
        return design.get(slot);
    }

    public float getAtk() {
        return atk;
    }

    public void setAtk(float atk) {
        this.atk = atk;
    }

    public float getDef() {
        return def;
    }

    public void setDef(float def) {
        this.def = def;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public HashMap<CurrencyTypes, Payment> getCost() {
        return paymentList;
    }

    public void setCost(HashMap<CurrencyTypes, Payment> cost) {
        this.paymentList = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float calculateTime() {
        return design.keySet().size();
    }

    public Country getCountry() {
        return country;
    }
}
