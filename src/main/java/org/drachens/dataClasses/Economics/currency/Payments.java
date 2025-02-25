package org.drachens.dataClasses.Economics.currency;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Payments implements Saveable {
    private final HashSet<Payment> payments;

    public Payments(List<Payment> payments) {
        this.payments = new HashSet<>(payments);
    }

    public Payments(Payments payments) {
        this.payments = new HashSet<>(payments.getPayments());
    }

    public Payments(Payment... payments) {
        this.payments = new HashSet<>(List.of(payments));
    }

    public void addPayment(Payment payment) {
        payments.add(payment.clone());
    }

    public void addPayments(Payments payments) {
        payments.getPayments().forEach(this::addPayment);
    }

    public void minusPayment(Payment payment) {
        payments.add(new Payment(payment.getCurrencyType(), -payment.getAmount()));
    }

    public void minusPayments(Payments payments) {
        payments.getPayments().forEach(this::minusPayment);
    }

    public HashSet<Payment> getPayments() {
        HashSet<Payment> newList = new HashSet<>();
        payments.forEach(payment -> newList.add(payment.clone()));
        return newList;
    }

    public void multiply(float multiply) {
        payments.forEach(payment -> payment.multiply(multiply));
    }

    public Component getMessages() {
        List<Component> comps = new ArrayList<>();
        payments.forEach(payment -> {
            comps.add(payment.getShownMessage());
            comps.add(Component.newline());
        });
        return Component.text()
                .append(comps)
                .build();
    }

    public void compress() {
        HashMap<CurrencyTypes, Float> amountHashMap = new HashMap<>();
        payments.forEach(payment -> {
            float current = amountHashMap.getOrDefault(payment.getCurrencyType(), 0.0f);
            current += payment.getAmount();
            amountHashMap.put(payment.getCurrencyType(), current);
        });
    }

    public void divide(Payments payments){
        getPayments().forEach(payment -> payments.getPayments().forEach(payment::divide));
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
