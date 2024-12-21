package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Economics.currency.Payment;

import net.minestom.server.item.ItemStack;

public class DivisionType {
    private String name;
    private float atk;
    private float def;
    private float org;
    private float speed;
    private float trainingTime;
    private Payment payment;
    private ItemStack icon;

    public DivisionType(String name, float atk, float def, float org, float speed, float trainingTime, Payment payment, ItemStack icon) {
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.org = org;
        this.speed = speed;
        this.payment = payment;
        this.icon=icon;
        this.trainingTime = trainingTime;
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

    public Payment getCost() {
        return payment;
    }

    public void setCost(Payment payment) {
        this.payment = payment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIcon(){
        return icon;
    }

    public float getTrainingTime(){
        return trainingTime;
    }
}
