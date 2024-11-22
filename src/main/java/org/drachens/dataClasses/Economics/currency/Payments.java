package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class Payments {
    private final List<Payment> payments;

    public Payments(Payments payments) {
        this.payments = payments.getPayments();
    }

    public Payments(Payment... payments) {
        this.payments = new ArrayList<>(List.of(payments));
    }

    public void addPayment(Payment payment){
        payments.add(payment.clone());
    }

    public void addPayments(Payments payments){
        payments.getPayments().forEach(this::addPayment);
    }

    public List<Payment> getPayments() {
        List<Payment> newList = new ArrayList<>();
        payments.forEach(payment -> newList.add(payment.clone()));
        return newList;
    }

    public void multiply(float multiply) {
        payments.forEach((payment -> payment.multiply(multiply)));
    }

    public void multiply(CurrencyTypes currencyType ,CurrencyBoost currencyBoost) {
        payments.forEach((payment ->{
            if (payment.getCurrencyType()==currencyType)payment.multiply(currencyBoost.getBoost());
        }));
    }

    public Component getMessages() {
        List<Component> comps = new ArrayList<>();
        payments.forEach(payment -> {
            comps.add(Component.text(payment.getAmount()));
            comps.add(payment.getCurrencyType().getSymbol());
            comps.add(Component.newline());
        });
        return Component.text()
                .append(comps)
                .build();
    }
}
