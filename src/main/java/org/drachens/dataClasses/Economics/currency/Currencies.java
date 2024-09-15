package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;

public class Currencies implements Cloneable {
    private final CurrencyTypes currencyType;
    private float amount;

    public Currencies(CurrencyTypes currencyType, float amount) {
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

    public void minus(float minus) {
        amount -= minus;
    }
    public Currencies clone() {
        try {
            return (Currencies) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
