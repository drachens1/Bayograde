package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;
import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;

public class Payment implements Cloneable {
    private final CurrencyTypes currencyType;
    private float amount;
    private Component message;

    public Payment(CurrencyEnum currencyEnum, float amount) {
        currencyType = currencyEnum.getCurrencyType();
        this.amount = amount;
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

    public Payment(Currencies currencies) {
        this.currencyType = currencies.getCurrencyType();
        this.amount = currencies.getAmount();
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

    public Payment minusMaxAmount(Payment payment) {
        if (currencyType != payment.getCurrencyType()) return new Payment(payment.currencyType, 0f);
        if (amount > payment.amount) return new Payment(payment.currencyType, 0f);
        amount -= payment.amount;
        Payment p = new Payment(payment.currencyType, Math.abs(amount));
        amount = 0f;
        return p;
    }

    public void multiply(float multiply) {
        amount *= multiply;
    }

    @Override
    public Payment clone() {
        try {
            return (Payment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
