package org.drachens.dataClasses.Economics;

import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.BoostEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.temporary.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Vault {
    private Country country;
    private final HashMap<CurrencyTypes, Currencies> amount;
    private final List<Loan> loans;

    public Vault(HashMap<CurrencyTypes, Currencies> startingCurrencies) {
        this.amount = new HashMap<>(startingCurrencies);
        this.loans = new ArrayList<>();
    }

    public void setCountry(Country country) {
        this.country = country;
        onCountrySet(country);
    }

    public abstract void onCountrySet(Country country);

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

    public void minusPayment(Payment payment, Country beneficiary) {
        beneficiary.getVault().addPayment(payment);
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

    public Payment minusMaximumPossible(Payment payment, Country beneficiary) {
        if (canMinus(payment)) {
            minusPayment(payment, beneficiary);
            return new Payment(payment.getCurrencyType(), 0);
        }
        CurrencyTypes currencyTypes = payment.getCurrencyType();
        Currencies currencies = amount.get(currencyTypes);
        Payment copy = payment.clone();
        Payment minus = new Payment(currencyTypes, currencies.getAmount());
        copy.remove(minus);
        currencies.minus(payment);
        Payment p = new Payment(currencyTypes, Math.abs(currencies.getAmount()));
        currencies.set(0f);
        return p;
    }

    public Payment minusMaximumPossible(Payment payment) {
        if (canMinus(payment)) {
            minusPayment(payment);
            return new Payment(payment.getCurrencyType(), 0);
        }
        CurrencyTypes currencyTypes = payment.getCurrencyType();
        Currencies currencies = amount.get(currencyTypes);
        Payment copy = payment.clone();
        Payment minus = new Payment(currencyTypes, currencies.getAmount());
        copy.remove(minus);
        currencies.minus(minus);
        return copy;
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    public void minusThenLoan(Payment payment, Country from) {
        if (canMinus(payment)) return;
        Payment returned = minusMaximumPossible(payment, from);
        addLoan(new Loan(returned, 10, from, country));
    }

    public void calculateIncrease() {
        Factory factory = (Factory) BuildingEnum.factory.getBuildTypes();
        List<Building> buildings = country.getBuildings(BuildingEnum.factory);
        if (buildings == null) return;

        Payments toCountry = new Payments();
        Payments toOverlord = new Payments();
        boolean overlord = country.getOverlord() != null;

        buildings.forEach(building -> {
            Payments payments = factory.generate(building);
            payments.multiply(country.getBoost(BoostEnum.production));
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
        loans.forEach(Loan::payThisWeek);
        extraCalcIncrease();
    }

    public abstract void extraCalcIncrease();

    public boolean canMinus(Payment payment) {
        Currencies currency = amount.get(payment.getCurrencyType());
        return currency != null && currency.getAmount() >= payment.getAmount();
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
        List<Currencies> currencies = new ArrayList<>();
        currencies.addAll(amount.values());
        currencies.addAll(getCustomCurrencies());
        return currencies;
    }

    public float getAmount(CurrencyTypes currencyTypes){
        return amount.get(currencyTypes).getAmount();
    }

    protected abstract List<Currencies> getCustomCurrencies();

    public List<Loan> getLoans() {
        return loans;
    }
}
