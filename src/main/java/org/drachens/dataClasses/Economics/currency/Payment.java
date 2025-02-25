package org.drachens.dataClasses.Economics.currency;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.interfaces.Saveable;

@Getter
public class Payment implements Cloneable, Saveable {
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

    public Component getShownMessage(){
        return Component.text()
                .append(Component.text(this.amount))
                .append(this.currencyType.getSymbol())
                .build();
    }

    public boolean add(Payment payment) {
        if (payment.currencyType.equals(currencyType)) {
            amount += payment.amount;
            return true;
        }
        return false;
    }

    public void remove(Payment payment) {
        if (payment.currencyType.equals(currencyType)) {
            amount -= payment.amount;
        }
    }

    public Payment minusMaxAmount(Payment payment) {
        if (currencyType != payment.currencyType) return new Payment(payment.currencyType, 0.0f);
        if (amount > payment.amount) return new Payment(payment.currencyType, 0.0f);
        amount -= payment.amount;
        Payment p = new Payment(payment.currencyType, Math.abs(amount));
        amount = 0.0f;
        return p;
    }

    public void multiply(float multiply) {
        amount *= multiply;
    }

    public void divide(Payment payment) {
        if (payment.currencyType!=currencyType)return;
        amount /= payment.getAmount();
    }


    @Override
    public Payment clone() {
        try {
            return (Payment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("currency",new JsonPrimitive(currencyType.getIdentifier()));
        jsonObject.add("amount",new JsonPrimitive(amount));
        return jsonObject;
    }
}
