package org.drachens.dataClasses.Economics;

import lombok.Getter;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.generalGame.factory.Factory;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public abstract class Vault implements Saveable {
    protected final HashMap<CurrencyTypes, Currencies> amount;
    protected final List<Loan> loans;
    protected Country country;

    protected Vault(HashMap<CurrencyTypes, Currencies> startingCurrencies) {
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
            if (null == existingCurrency) {
                return new Currencies(key, payment.getAmount());
            }
            existingCurrency.add(payment);
            return existingCurrency;
        });
    }

    public void minusPayment(Payment payment) {
        System.out.println("Minus payment from vault");
        amount.compute(payment.getCurrencyType(), (key, existingCurrency) -> {
            if (null == existingCurrency) {
                return new Currencies(key, -payment.getAmount());
            }
            float prev_value = payment.getAmount();
            float amount = existingCurrency.getAmount();
            existingCurrency.minus(payment);
            System.out.println("Payment amount: " + prev_value + " pre "+ amount + " post " + existingCurrency.getAmount());
            return existingCurrency;
        });
    }

    public void minusPayment(Payment payment, Country beneficiary) {
        System.out.println("Minus payment from beneficiary vault");
        beneficiary.getEconomy().getVault().addPayment(payment);
        amount.compute(payment.getCurrencyType(), (key, existingCurrency) -> {
            if (null == existingCurrency) {
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
        currencies.set(0.0f);
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

    public Payments minusMaximumPossible(Payments payment) {
        List<Payment> payments = new ArrayList<>();
        payment.getPayments().forEach(payment1 -> {
            payments.add(minusMaximumPossible(payment1));
        });
        return new Payments(payments);
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
        List<Building> buildings = country.getEconomy().getBuildingType(BuildingEnum.factory);
        extraCalcIncrease();
        if (null == buildings) return;

        Payments toCountry = new Payments();
        Payments toOverlord = new Payments();
        boolean overlord = null != this.country.getInfo().getOverlord();

        buildings.forEach(building -> {
            Payment payment = factory.generate(building);
            payment.multiply(country.getBoost(BoostEnum.production));
            if (overlord) {
                Payments toCountry2 = new Payments(payment);
                toCountry2.multiply(0.8f);
                payment.multiply(0.2f);
                toOverlord.addPayment(payment);
                toCountry.addPayments(toCountry2);
            } else {
                toCountry.addPayment(payment);
            }
        });
        addPayments(toCountry);

        if (overlord) {
            country.getInfo().getOverlord().addPayments(toOverlord);
        }
        loans.forEach(Loan::payThisWeek);
    }

    public abstract void extraCalcIncrease();

    public boolean canMinus(Payment payment) {
        Currencies currency = amount.get(payment.getCurrencyType());
        return null != currency && currency.getAmount() >= payment.getAmount();
    }

    public boolean canMinus(Payments payments) {
        for (Payment payment : payments.getPayments()) {
            Currencies currency = amount.get(payment.getCurrencyType());
            if (null == currency || currency.getAmount() <= payment.getAmount()) {
                return false;
            }
        }
        return true;
    }

    public float subtractMaxAmountPossible(Payment payment) {
        if (canMinus(payment)) {
            minusPayment(payment);
            return 0.0f;
        }
        Currencies currency = amount.get(payment.getCurrencyType());
        if (null != currency) {
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

    public float getAmount(CurrencyTypes currencyTypes) {
        return amount.get(currencyTypes).getAmount();
    }

    protected abstract List<Currencies> getCustomCurrencies();
}
