package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Cost;

import java.util.List;

public class DivisionDesign {
    private String[] design;
    private float atk;
    private float def;
    private float org;
    private float speed;
    private List<Cost> cost;
    private final Country country;
    private String name;
    public DivisionDesign(String name ,String[] design, float atk, float def, float org, float speed, List<Cost> cost, Country country){
        this.design = design;
        this.atk = atk;
        this.def = def;
        this.org = org;
        this.speed = speed;
        this.cost = cost;
        this.country = country;
        this.name = name;
    }
    public String[] getDesign(){
        return design;
    }
    public void setDesign(String[] design){
        this.design = design;
    }
    public float getAtk(){
        return atk;
    }
    public void setAtk(float atk) {
        this.atk = atk;
    }
    public float getDef(){
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
    public List<Cost> getCost(){
        return cost;
    }
    public void setCost(List<Cost> cost) {
        this.cost = cost;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}
