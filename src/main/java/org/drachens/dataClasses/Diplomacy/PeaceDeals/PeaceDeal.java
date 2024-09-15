package org.drachens.dataClasses.Diplomacy.PeaceDeals;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Cost;
import org.drachens.dataClasses.Provinces.Province;

import java.util.List;

public class PeaceDeal {
    private final Country toCountry;
    private final Country fromCountry;
    private final List<Cost> payment; //The cost to be paid if it is accepted
    private final List<Province> provinces; //The provinces to be given up if it is accepted
    private final float expiration; //White peace untill it either expires or is denied

    public PeaceDeal(Country toCountry, Country fromCountry, List<Cost> payment, List<Province> provinces, float expiration) {
        this.toCountry = toCountry;
        this.fromCountry = fromCountry;
        this.payment = payment;
        this.provinces = provinces;
        this.expiration = expiration;
    }

    public Country getToCountry() {
        return toCountry;
    }

    public Country getFromCountry() {
        return fromCountry;
    }

    public List<Cost> getPayment() {
        return payment;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public float getExpiration() {
        return expiration;
    }
}
