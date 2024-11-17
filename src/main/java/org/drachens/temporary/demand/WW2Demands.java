package org.drachens.temporary.demand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;

import java.util.ArrayList;
import java.util.List;

public class WW2Demands extends Demand {
    private List<Country> demandedAnnexation = new ArrayList<>();
    private List<Province> demandedProvinces = new ArrayList<>();
    private List<Country> demandedPuppets = new ArrayList<>();
    private List<Payment> demandedPayments = new ArrayList<>();

    private List<Payment> offeredPayments = new ArrayList<>();
    private List<Country> offeredPuppets = new ArrayList<>();
    private List<Province> offeredProvinces = new ArrayList<>();
    private List<Country> offeredAnnexation = new ArrayList<>();

    public List<Country> getDemandedAnnexation() {
        return new ArrayList<>(demandedAnnexation);
    }

    public List<Province> getDemandedProvinces() {
        return new ArrayList<>(demandedProvinces);
    }

    public List<Country> getDemandedPuppets() {
        return new ArrayList<>(demandedPuppets);
    }

    public List<Payment> getDemandedPayments() {
        return new ArrayList<>(demandedPayments);
    }

    public List<Payment> getOfferPayments() {
        return new ArrayList<>(offeredPayments);
    }

    public List<Country> getOfferPuppets() {
        return new ArrayList<>(offeredPuppets);
    }

    public List<Province> getOfferProvinces() {
        return new ArrayList<>(offeredProvinces);
    }

    public List<Country> getOfferedAnnexation() {
        return new ArrayList<>(offeredAnnexation);
    }

    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private final Player p;

    private boolean peace = false;
    public WW2Demands(Country from, Country to, Player p) {
        super(from, to);
        this.p = p;
    }

    @Override
    public Component description() {
        List<Component> comps = new ArrayList<>();
        if (!(offeredPuppets.isEmpty() && offeredPayments.isEmpty() && offeredProvinces.isEmpty() && offeredAnnexation.isEmpty())){
            comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text("Demanded: ",NamedTextColor.RED,TextDecoration.BOLD,TextDecoration.UNDERLINED))
                    .build());

            if (!offeredPuppets.isEmpty()){
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Puppets: "))
                        .build());
                for (Country c : offeredPuppets){
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(c.getNameComponent())
                            .build());
                }
            }

            if (!offeredPayments.isEmpty()){
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Payments: "))
                        .build());
                for (Payment p : offeredPayments){
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(p.getAmount()))
                            .append(p.getCurrencyType().getSymbol())
                            .build());
                }
            }

            if (!offeredProvinces.isEmpty()){
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Provinces: "))
                        .build());
                for (Province p : offeredProvinces){
                    Pos pos = p.getPos();
                    String merge = pos.x()+", "+pos.y();
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(merge))
                            .build());
                }
            }

            if (!offeredAnnexation.isEmpty()){
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Annexation: "))
                        .build());
                for (Country c : offeredAnnexation){
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(c.getNameComponent())
                            .build());
                }
            }
        }

        if (!(demandedPuppets.isEmpty() && demandedPayments.isEmpty() && demandedProvinces.isEmpty() && demandedAnnexation.isEmpty())){
            comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text("Offered: ",NamedTextColor.GREEN,TextDecoration.BOLD,TextDecoration.UNDERLINED))
                    .build());

            if (!demandedPuppets.isEmpty()){
                comps.add(Component.text()
                                .appendNewline()
                                .append(Component.text("- Demanded Puppets: "))
                        .build());
                for (Country c : demandedPuppets){
                    comps.add(Component.text()
                                    .appendNewline()
                                    .append(Component.text(" - "))
                                    .append(c.getNameComponent())
                            .build());
                }
            }

            if (!demandedPayments.isEmpty()){
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Demanded Payments: "))
                        .build());
                for (Payment p : demandedPayments){
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(p.getAmount()))
                            .append(p.getCurrencyType().getSymbol())
                            .build());
                }
            }

            if (!demandedProvinces.isEmpty()){
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Demanded Provinces: "))
                        .build());
                for (Province p : demandedProvinces){
                    Pos pos = p.getPos();
                    String merge = pos.x()+", "+pos.y();
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(merge))
                            .build());
                }
            }

            if (!demandedAnnexation.isEmpty()){
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Demanded Annexation: "))
                        .build());
                for (Country c : demandedAnnexation){
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(c.getNameComponent())
                            .build());
                }
            }
        }
        return Component.text()
                .append(Component.text("_________/", NamedTextColor.BLUE))
                .append(Component.text("Demand", NamedTextColor.RED,TextDecoration.BOLD))
                .append(Component.text("\\_________\n", NamedTextColor.BLUE))
                .append(comps)
                .appendNewline()
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

    }

    @Override
    public void copyButOpposite(Demand d) {
        if (!(d instanceof WW2Demands demand))return;
        this.offeredProvinces=demand.getDemandedProvinces();
        this.offeredAnnexation=demand.getDemandedAnnexation();
        this.offeredPayments=demand.getDemandedPayments();
        this.offeredPuppets=demand.getDemandedPuppets();

        this.demandedProvinces=demand.getOfferProvinces();
        this.demandedAnnexation=demand.getOfferedAnnexation();
        this.demandedPayments=demand.getOfferPayments();
        this.demandedPuppets=demand.getOfferPuppets();
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
        offeredProvinces.add(province);
    }
    public void addPuppetOffer(Country country){
        offeredPuppets.add(country);
    }
    public void addPaymentOffer(Payment payment){
        offeredPayments.add(payment);
    }
    public void resetOfferProvinces(){
        offeredProvinces.clear();
    }
    public void resetOfferPuppets(){
        offeredPuppets.clear();
    }
    public void resetOfferPayments(){
        offeredPayments.clear();
    }
    public void resetOfferAnnexation(){
        offeredAnnexation.clear();
    }
    public void setPeace(boolean p){
        peace=p;
    }
    public boolean getPeace(){
        return peace;
    }
}
