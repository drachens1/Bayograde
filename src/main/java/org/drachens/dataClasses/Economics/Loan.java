package org.drachens.dataClasses.Economics;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;

public class Loan {
    private final Country fromCountry;
    private final Country toCountry;
    private final CurrencyTypes currencyTypes;
    private float balanceToPayOff;
    private final float perWeek;
    private final int termlength;

    public Loan(Payment payment, int termLength, Country from, Country to) {
        this.balanceToPayOff = payment.getAmount();
        this.currencyTypes = payment.getCurrencyType();
        this.fromCountry = from;
        this.toCountry = to;
        this.termlength = termLength;
        perWeek = balanceToPayOff / termLength;
    }

    public Loan(float payment, CurrencyTypes currencyTypes, float interest, int termLength, Country from, Country to) {
        to.getVault().addPayment(new Payment(currencyTypes, payment));
        this.balanceToPayOff = payment * (interest / 100);
        this.currencyTypes = currencyTypes;
        this.fromCountry = from;
        this.toCountry = to;
        this.termlength = -1;
        perWeek = balanceToPayOff / termLength;
    }

    public void payOff(Payment payment) {
        if (payment.getCurrencyType() != currencyTypes) return;
        balanceToPayOff -= payment.getAmount();
    }

    public void payThisWeek() {
        if (balanceToPayOff <= 0) {
            return;
        }
        balanceToPayOff -= perWeek;
        Payment payment = new Payment(currencyTypes, perWeek);
        toCountry.getVault().minusPayment(payment);
        fromCountry.getVault().addPayment(payment);
    }

    public void close() {
        toCountry.getVault().removeLoan(this);
    }

    public Component getDescription() {
        return Component.text()
                .append(Component.text("____/Loan\\_____"))
                .append(Component.text("From: "))
                .append(fromCountry.getNameComponent())
                .appendNewline()
                .append(Component.text("To: "))
                .append(toCountry.getNameComponent())
                .appendNewline()
                .append(Component.text("ToPay: "))
                .append(Component.text(balanceToPayOff))
                .appendNewline()
                .append(Component.text("Weeks to pay: "))
                .append(Component.text(termlength))
                .build();
    }

    public Country getFromCountry() {
        return fromCountry;
    }

    public Country getToCountry() {
        return toCountry;
    }
}
