package org.drachens.temporary.demand;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.demand.Demand;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Provinces.Province;

import java.util.ArrayList;
import java.util.List;

public class WW2Demands extends Demand {
    private final List<Country> demandedAnnexation = new ArrayList<>();
    private final List<Province> demandedProvinces = new ArrayList<>();
    private final List<Country> demandedPuppets = new ArrayList<>();
    private final List<Payment> demandedPayments = new ArrayList<>();

    private final List<Payment> OfferPayments = new ArrayList<>();
    private final List<Country> OffernPuppets = new ArrayList<>();
    private final List<Province> OffernProvinces = new ArrayList<>();
    private final List<Country> OfferedAnnexation = new ArrayList<>();

    private boolean peace = false;
    public WW2Demands(Country from, Country to) {
        super(from, to);
    }

    @Override
    public Component description() {
        return null;
    }

    @Override
    public void ifAccepted() {

    }

    @Override
    public void ifDenied() {

    }

    @Override
    protected void onCompleted() {
        getToCountry().sendMessage(description());
    }
    public void addProvinceDemand(Province province){
        demandedProvinces.add(province);
    }
    public void addPuppetDemand(Country country){
        demandedPuppets.add(country);
    }
    public void addPaymentDemand(Payment payment){
        demandedPayments.add(payment);
    }
    public void resetDemandedProvinces(){
        demandedProvinces.clear();
    }
    public void resetDemandedPuppets(){
        demandedPuppets.clear();
    }
    public void resetDemandedPayments(){
        demandedPayments.clear();
    }
    public void resetDemandedAnnexation(){
        demandedAnnexation.clear();
    }
    public void addProvinceOffer(Province province){
        OffernProvinces.add(province);
    }
    public void addPuppetOffer(Country country){
        OffernPuppets.add(country);
    }
    public void addPaymentOffer(Payment payment){
        OfferPayments.add(payment);
    }
    public void resetOfferProvinces(){
        OffernProvinces.clear();
    }
    public void resetOfferPuppets(){
        OffernPuppets.clear();
    }
    public void resetOfferPayments(){
        OfferPayments.clear();
    }
    public void resetOfferAnnexation(){
        OfferedAnnexation.clear();
    }
    public void setPeace(boolean p){
        peace=p;
    }
    public boolean getPeace(){
        return peace;
    }
}
