package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Payment;

import java.util.List;

public class DivisionDesign {
    private final Country country;
    private String[] design;
    private float atk;
    private float def;
    private float org;
    private float speed;
    private List<Payment> paymentList;
    private String name;

    public DivisionDesign(String name, String[] design, float atk, float def, float org, float speed, List<Payment> paymentList, Country country) {
        this.design = design;
        this.atk = atk;
        this.def = def;
        this.org = org;
        this.speed = speed;
        this.paymentList = paymentList;
        this.country = country;
        this.name = name;
    }

    public String[] getDesign() {
        return design;
    }

    public void setDesign(String[] design) {
        this.design = design;
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

    public float getOrg() {
        return org;
    }

    public void setOrg(float org) {
        this.org = org;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public List<Payment> getCost() {
        return paymentList;
    }

    public void setCost(List<Payment> cost) {
        this.paymentList = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
