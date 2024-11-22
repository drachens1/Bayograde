package org.drachens.dataClasses.Economics;

import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;

public class Loan {
    private final CurrencyTypes currencyTypes;
    private float balanceToPayOff;

    public Loan(float toPay, CurrencyTypes currencyTypes) {
        this.balanceToPayOff = toPay;
        this.currencyTypes = currencyTypes;
    }

    public void payOff(Payment payment) {
        if (payment.getCurrencyType() != currencyTypes) return;
        balanceToPayOff += payment.getAmount();
    }
}
