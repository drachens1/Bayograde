package org.drachens.dataClasses.Economics;

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
import java.util.Map;

public class Vault {
    private final Country country;
    private final Map<CurrencyTypes, Currencies> amount;
    private final List<Loan> loans;

    public Vault(Country country, Map<CurrencyTypes, Currencies> startingCurrencies) {
        this.country = country;
        this.amount = new HashMap<>(startingCurrencies);
        this.loans = new ArrayList<>();
    }

    public void setCurrency(Currencies currency) {
        amount.put(currency.getCurrencyType(), currency);
    }

    public void addPayment(Payment payment) {
        amount.compute(payment.getCurrencyType(), (key, existingCurrency) -> {
            if (existingCurrency == null) {
                return new Currencies(key, payment.getAmount());
            }
            existingCurrency.add(payment);
            return existingCurrency;
        });
    }

    public void minusPayment(Payment payment) {
        amount.compute(payment.getCurrencyType(), (key, existingCurrency) -> {
            if (existingCurrency == null) {
                return new Currencies(key, -payment.getAmount());
            }
            existingCurrency.minus(payment);
            return existingCurrency;
        });
    }

    public void addPayments(Payments payments) {
        payments.getPayments().forEach(this::addPayment);
    }

    public void minusPayments(Payments payments) {
        payments.getPayments().forEach(this::minusPayment);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void calculateIncrease() {
        Factory factory = (Factory) ContinentalManagers.defaultsStorer.buildingTypes.getBuildType("factory");
        List<Building> buildings = country.getBuildings(factory);
        if (buildings == null) return;

        Payments toCountry = new Payments();
        Payments toOverlord = new Payments();
        boolean overlord = country.getOverlord() != null;

        buildings.forEach(building -> {
            Payments payments = factory.generate(building);
            if (overlord) {
                Payments toCountry2 = new Payments(payments);
                toCountry2.multiply(0.8f);
                payments.multiply(0.2f);
                toOverlord.addPayments(payments);
                toCountry.addPayments(toCountry2);
            } else {
                toCountry.addPayments(payments);
            }
        });
        addPayments(toCountry);

        if (overlord) {
            country.getOverlord().addPayments(toOverlord);
        }
    }

    public boolean canMinus(Payment payment) {
        Currencies currency = amount.get(payment.getCurrencyType());
        return currency != null && currency.getAmount() > payment.getAmount();
    }

    public boolean canMinus(Payments payments) {
        for (Payment payment : payments.getPayments()) {
            Currencies currency = amount.get(payment.getCurrencyType());
            if (currency == null || currency.getAmount() <= payment.getAmount()) {
                return false;
            }
        }
        return true;
    }

    public float subtractMaxAmountPossible(Payment payment) {
        if (canMinus(payment)) {
            minusPayment(payment);
            return 0f;
        }
        Currencies currency = amount.get(payment.getCurrencyType());
        if (currency != null) {
            return payment.getAmount() - currency.getAmount();
        }
        return payment.getAmount();
    }

    public List<Currencies> getCurrencies() {
        return new ArrayList<>(amount.values());
    }
}
