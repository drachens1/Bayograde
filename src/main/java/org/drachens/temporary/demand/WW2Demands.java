package org.drachens.temporary.demand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.demand.Demand;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;

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

    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private final Player p;

    private boolean peace = false;
    public WW2Demands(Country from, Country to, Player p) {
        super(from, to);
        this.p = p;
    }

    @Override
    public Component description() {
        return Component.text()
                .append(Component.text("_________/", NamedTextColor.BLUE))
                .append(Component.text("Demand", NamedTextColor.RED,TextDecoration.BOLD))
                .append(Component.text("\\_________\n", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text()
                        .append(Component.text("[Accept]",NamedTextColor.GREEN, TextDecoration.BOLD))
                        .hoverEvent(Component.text("Click to accept the demands",NamedTextColor.GREEN))
                        .clickEvent(ClickEvent.runCommand("/country diplomacy demand accept"))
                        .build())
                .append(Component.text()
                        .append(Component.text(" [Refuse]",NamedTextColor.RED, TextDecoration.BOLD))
                        .hoverEvent(Component.text("Click to refuse the demands",NamedTextColor.RED))
                        .clickEvent(ClickEvent.runCommand("/country diplomacy demand refuse"))
                        .build())
                .build();
    }

    @Override
    public void ifAccepted() {

    }

    @Override
    public void ifDenied() {

    }

    @Override
    protected void onCompleted() {
        Country to = getToCountry();
        to.sendMessage(description());
        to.sendDemand(this);
        demandManager.removeActive(p);
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
