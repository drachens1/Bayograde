package org.drachens.dataClasses.Economics.currency;

import com.google.gson.JsonElement;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.interfaces.Saveable;

@Getter
public class Currencies implements Cloneable, Saveable {
    private final CurrencyTypes currencyType;
    private float amount;

    public Currencies(CurrencyTypes currencyType, float amount) {
        this.currencyType = currencyType;
        this.amount = amount;
    }

    public Currencies(CurrencyEnum currencyEnum, float amount) {
        currencyType = currencyEnum.getCurrencyType();
        this.amount = amount;
    }

    public Component getName() {
        return currencyType.getName();
    }

    public void add(float add) {
        amount += add;
    }

    public void add(Payment payment) {
        amount += payment.getAmount();
    }

    public void minus(Payment payment) {
        amount -= payment.getAmount();
    }

    public void set(float amount) {
        this.amount = amount;
    }

    public void minus(float minus) {
        amount -= minus;
    }

    @Override
    public Currencies clone() {
        try {
            return (Currencies) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
