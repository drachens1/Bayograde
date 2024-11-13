package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;

public class Payment {
    private final CurrencyTypes currencyType;
    private float amount;
    private Component message;

    public Payment(Payment payment) {
        this.amount = payment.getAmount();
        this.currencyType = payment.getCurrencyType();
        this.message = payment.getMessage();
    }

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

    public void multiply(int multiply) {
        amount *= multiply;
    }

    public void multiply(float multiply) {
        amount *= multiply;
    }
}
