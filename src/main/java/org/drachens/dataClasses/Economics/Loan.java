package org.drachens.dataClasses.Economics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.interfaces.Saveable;

@Getter
public class Loan implements Saveable {
    private final Country fromCountry;
    private final Country toCountry;
    private final CurrencyTypes currencyTypes;
    private final float perWeek;
    private final int termlength;
    private float balanceToPayOff;

    public Loan(Payment payment, int termLength, Country from, Country to) {
        this.balanceToPayOff = payment.getAmount();
        this.currencyTypes = payment.getCurrencyType();
        this.fromCountry = from;
        this.toCountry = to;
        this.termlength = termLength;
        perWeek = balanceToPayOff / termLength;
    }

    public Loan(float payment, CurrencyTypes currencyTypes, float interest, int termLength, Country from, Country to) {
        to.getEconomy().getVault().addPayment(new Payment(currencyTypes, payment));
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
        toCountry.getEconomy().getVault().minusPayment(payment);
        fromCountry.getEconomy().getVault().addPayment(payment);
    }

    public void close() {
        toCountry.getEconomy().getVault().removeLoan(this);
    }

    public Component getDescription() {
        return Component.text()
                .append(Component.text("____/Loan\\_____"))
                .append(Component.text("From: "))
                .append(fromCountry.getInfo().getOriginalName())
                .appendNewline()
                .append(Component.text("To: "))
                .append(toCountry.getInfo().getOriginalName())
                .appendNewline()
                .append(Component.text("ToPay: "))
                .append(Component.text(balanceToPayOff))
                .appendNewline()
                .append(Component.text("Weeks to pay: "))
                .append(Component.text(termlength))
                .build();
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("from",new JsonPrimitive(fromCountry.getName()));
        jsonObject.add("to",new JsonPrimitive(toCountry.getName()));
        jsonObject.add("currency",new JsonPrimitive(currencyTypes.getIdentifier()));
        jsonObject.add("perweek",new JsonPrimitive(perWeek));
        jsonObject.add("termlength",new JsonPrimitive(termlength));
        jsonObject.add("balanceToPayOff",new JsonPrimitive(balanceToPayOff));
        return jsonObject;
    }
}
