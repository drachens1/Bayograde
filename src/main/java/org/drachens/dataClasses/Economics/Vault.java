package org.drachens.dataClasses.Economics;

import net.kyori.adventure.text.Component;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.temporary.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vault {
    private final Country country;
    private final HashMap<CurrencyTypes, Currencies> amount = new HashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    public Vault(Country country, HashMap<CurrencyTypes, Currencies> startingCurrencies) {
        this.country = country;
        this.amount.putAll(startingCurrencies);
    }

    public void setCurrency(Currencies currency){
        amount.put(currency.getCurrencyType(),currency);
    }

    public void addPayment(Payment payment){
        if (!amount.containsKey(payment.getCurrencyType())) {
            amount.put(payment.getCurrencyType(),new Currencies(payment.getCurrencyType(),payment.getAmount()));
        }else {
            amount.get(payment.getCurrencyType()).add(payment);
        }
    }

    public void minusPayment(Payment payment){
        if (!amount.containsKey(payment.getCurrencyType())) {
            amount.put(payment.getCurrencyType(),new Currencies(payment.getCurrencyType(),-payment.getAmount()));
        }else {
            amount.get(payment.getCurrencyType()).minus(payment);
        }
    }

    public void addPayments(Payments payments){
        payments.foreach(this::addPayment);
    }

    public void minusPayments(Payments payments){
        payments.foreach(this::minusPayment);
    }

    public void addLoan(Loan loan){
        loans.add(loan);
    }

    public void calculateIncrease(){
        Factory factory = (Factory) ContinentalManagers.defaultsStorer.buildingTypes.getBuildType("factory");
        List<Building> buildings =  country.getBuildings(factory);
        if (buildings==null)return;
        Payments toCountry = new Payments();
        Payments toOverlord = new Payments();
        boolean overlord = country.getOverlord()!=null;
        buildings.forEach(building -> {
            Payments payments = factory.generate(building);
            payments.multiply(country.getTotalProductionBoost());
            country.getEconomyBoosts().forEach(payments::multiply);
            country.createFloatingText(building, Component.text()
                    .append(payments.getMessages())
                    .build());
            if (overlord){
                Payments toCountry2 = new Payments(payments);
                toCountry2.multiply(0.8f);
                Payments toOverlord2 = new Payments(payments);
                toOverlord2.multiply(0.2f);
                toOverlord.addPayments(toOverlord2);
                toCountry.addPayments(toCountry2);
            }else {
                toCountry.addPayments(payments);
            }
        });
        country.addPayments(toCountry);
        if (overlord){
            country.getOverlord().addPayments(toOverlord);
        }
    }

    public boolean canMinus(Payment payment){
        if (!amount.containsKey(payment.getCurrencyType()))return false;
        return amount.get(payment.getCurrencyType()).getAmount()>payment.getAmount();
    }

    public boolean canMinus(Payments payments){
        for (Payment payment : payments.getPayments()){
            if (!amount.containsKey(payment.getCurrencyType()))return false;
            if (!(amount.get(payment.getCurrencyType()).getAmount()>payment.getAmount()))return false;
        }
        return true;
    }

    public float subtractMaxAmountPossible(Payment payment){
        if (canMinus(payment)){
            minusPayment(payment);
            return 0f;
        }
        return amount.containsKey(payment.getCurrencyType()) ? payment.getAmount()-amount.get(payment.getCurrencyType()).getAmount() : payment.getAmount();
    }
}
