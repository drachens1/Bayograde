package org.drachens.dataClasses.Economics.currency;

public class Cost {
    private final CurrencyTypes currencyType;
    private float amount;

    public Cost(CurrencyTypes currencyType, float amount) {
        this.currencyType = currencyType;
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public CurrencyTypes getCurrencyType() {
        return currencyType;
    }

    public boolean add(Cost cost) {
        if (cost.getCurrencyType().equals(currencyType)) {
            amount += cost.getAmount();
            return true;
        }
        return false;
    }

    public boolean remove(Cost cost) {
        if (cost.getCurrencyType().equals(currencyType)) {
            amount -= cost.getAmount();
            return true;
        }
        return false;
    }
}
