package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Payments {
    private final List<Payment> payments;
    public Payments(Payments payments){
        List<Payment> payments1 = payments.getPayments();
        List<Payment> newPayments = new ArrayList<>();
        payments1.forEach(payment -> newPayments.add(new Payment(payment)));
        this.payments = newPayments;
    }
    public Payments(List<Payment> payments){
        this.payments = payments;
    }
    public Payments(Payment... payments){
        this.payments = List.of(payments);
    }
    public List<Payment> getPayments(){
        return payments;
    }
    public HashMap<CurrencyTypes, Currencies> addPayments(HashMap<CurrencyTypes, Currencies> toAdd){
        for (Payment payment : payments){
            if (toAdd.containsKey(payment.getCurrencyType())){
                toAdd.get(payment.getCurrencyType()).add(payment);
            }
        }
        return toAdd;
    }
    public HashMap<CurrencyTypes, Currencies> minusPayments(HashMap<CurrencyTypes, Currencies> toAdd){
        for (Payment payment : payments){
            if (toAdd.containsKey(payment.getCurrencyType())){
                toAdd.get(payment.getCurrencyType()).minus(payment);
            }
        }
        return toAdd;
    }
    public void multiply(int multiply){
        payments.forEach((payment -> payment.multiply(multiply)));
    }
    public void multiply(float multiply){
        payments.forEach((payment -> payment.multiply(multiply)));
    }
    public Component getMessages(){
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
