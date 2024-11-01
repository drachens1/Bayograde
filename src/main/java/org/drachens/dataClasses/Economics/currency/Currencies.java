package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;

public class Currencies implements Cloneable {
    private float boost;
    private final CurrencyTypes currencyType;
    private float amount;

    public Currencies(CurrencyTypes currencyType, float amount) {
        boost = 0f;
        this.currencyType = currencyType;
        this.amount = amount;
    }

    public CurrencyTypes getCurrencyType() {
        return currencyType;
    }

    public Component getName() {
        return currencyType.getName();
    }

    public float getAmount() {
        return amount;
    }

    public void add(float add) {
        amount += add;
    }

    public void add(Payment payment){
        amount+=payment.getAmount();
    }

    public void minus(Payment payment){
        amount-=payment.getAmount();
    }

    public void minus(float minus) {
        amount -= minus;
    }
    public void setBoost(float boost){
        this.boost = boost;
    }
    public Currencies clone() {
        try {
            return (Currencies) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public void addBoost(float boost){
        this.boost += boost;
    }
}
