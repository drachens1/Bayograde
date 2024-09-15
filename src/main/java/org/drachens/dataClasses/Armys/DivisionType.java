package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Economics.currency.Cost;

public class DivisionType {
    private String name;
    private float atk;
    private float def;
    private float org;
    private float speed;
    private Cost cost;

    public DivisionType(String name, float atk, float def, float org, float speed, Cost cost) {
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.org = org;
        this.speed = speed;
        this.cost = cost;
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

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
