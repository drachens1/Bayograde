package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Payments {
    private final List<Payment> payments;

    public Payments(Payments payments) {
        List<Payment> payments1 = payments.getPayments();
        List<Payment> newPayments = new ArrayList<>();
        payments1.forEach(payment -> newPayments.add(new Payment(payment)));
        this.payments = newPayments;
    }

    public void addPayment(Payment payment){
        payments.add(payment);
    }

    public void addPayments(Payments payments){
        this.payments.addAll(payments.getPayments());
    }

    public Payments(List<Payment> payments) {
        this.payments = payments;
    }

    public Payments(Payment... payments) {
        this.payments = List.of(payments);
    }

    public List<Payment> getPayments() {
        return payments;
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

    public void foreach(Consumer< ? super Payment> action){
        for (Payment payment : payments){
            action.accept(payment);
        }
    }
}
