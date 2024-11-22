package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;

public class Payment implements Cloneable {
    private final CurrencyTypes currencyType;
    private float amount;
    private Component message;

    public Payment(CurrencyTypes currencyType, float amount) {
        this.currencyType = currencyType;
        this.amount = amount;
    }

    public Payment(CurrencyTypes currencyType, float amount, Component message) {
        this.currencyType = currencyType;
        this.amount = amount;
        this.message = message;
    }

    public float getAmount() {
        return amount;
    }

    public CurrencyTypes getCurrencyType() {
        return currencyType;
    }

    public Component getMessage() {
        return message;
    }

    public boolean add(Payment payment) {
        if (payment.getCurrencyType().equals(currencyType)) {
            amount += payment.getAmount();
            return true;
        }
        return false;
    }

    public boolean remove(Payment payment) {
        if (payment.getCurrencyType().equals(currencyType)) {
            amount -= payment.getAmount();
            return true;
        }
        return false;
    }

    public void multiply(float multiply) {
        amount *= multiply;
    }

    @Override
    public Payment clone() {
        try {
            Payment clone = (Payment) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
