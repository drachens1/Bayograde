package org.drachens.temporary.demand;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.utils.PacketUtils;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;

import java.util.ArrayList;
import java.util.HashMap;
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

    private final HashMap<Province, Block> shownBlocks = new HashMap<>();

    private final List<Player> activePlayers = new ArrayList<>();

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

    private boolean peace = false;
    public WW2Demands(Country from, Country to) {
        super(from, to);
        CPlayer p = (CPlayer) from.getPlayerLeader();
        showPlayer(p);
    }

    @Override
    public Component description() {
        List<Component> comps = new ArrayList<>();
        if (!(offeredPuppets.isEmpty() && offeredPayments.isEmpty() && offeredProvinces.isEmpty() && offeredAnnexation.isEmpty())){
            comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text("Offered: ",NamedTextColor.GREEN,TextDecoration.BOLD,TextDecoration.UNDERLINED))
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
                            .append(Component.text("Demanded: ",NamedTextColor.RED,TextDecoration.BOLD,TextDecoration.UNDERLINED))
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
        Country to = getToCountry();
        Country from = getFromCountry();

        demandedAnnexation.forEach(country-> country.getOccupies().forEach(province -> province.setOccupier(from)));
        demandedPuppets.forEach(country -> {
            country.setOverlord(to);
            from.addPuppet(country);
        });
        demandedProvinces.forEach(province -> province.setOccupier(from));
        demandedPayments.forEach(payment -> {

        });
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

    public void showPlayer(CPlayer p){
        activePlayers.add(p);
        Country to = getToCountry();
        Country from = getFromCountry();
        List<Country> countries = new ArrayList<>(ContinentalManagers.world(to.getInstance()).countryDataManager().getCountries());
        countries.remove(to);
        countries.removeAll(to.getPuppets());
        countries.remove(from);
        countries.removeAll(from.getPuppets());
        countries.forEach(country -> country.getOccupies().forEach(province -> PacketUtils.sendPacket(p,new BlockChangePacket(province.getPos(),Block.GRAY_CONCRETE))));

        shownBlocks.forEach((province,block)-> PacketUtils.sendPacket(p,new BlockChangePacket(province.getPos(),block)));
    }

    InventoryManager inventoryManager = ContinentalManagers.inventoryManager;

    public void hidePlayer(CPlayer p){
        activePlayers.remove(p);
        ContinentalManagers.world(p.getInstance()).countryDataManager().getCountries().forEach(country -> country.reloadBlocksForPlayer(p));
        inventoryManager.assignInventory(p,"default");
    }

    public void showPlayerView(CPlayer p){
        Country to = getToCountry();
        Country from = getFromCountry();
        List<Country> countries = new ArrayList<>(ContinentalManagers.world(to.getInstance()).countryDataManager().getCountries());
        countries.remove(to);
        countries.removeAll(to.getPuppets());
        countries.remove(from);
        countries.removeAll(from.getPuppets());
        countries.forEach(country -> country.getOccupies().forEach(province -> PacketUtils.sendPacket(p,new BlockChangePacket(province.getPos(),Block.GRAY_CONCRETE))));

        shownBlocks.forEach((province,block)-> PacketUtils.sendPacket(p,new BlockChangePacket(province.getPos(),block)));
    }

    private void updateProvinceBlock(Province province, Block block) {
        shownBlocks.put(province, block);
        PacketUtils.sendGroupedPacket(activePlayers, new BlockChangePacket(province.getPos(), block));
    }

    private void processCountryDemand(Country country, Block block, boolean addDemand) {
        country.getOccupies().forEach(province -> {
            if (demandedProvinces.contains(province)) return;
            if (addDemand) {
                updateProvinceBlock(province, block);
            } else {
                Block newBlock = country.getBlockForProvince(province);
                if (demandedProvinces.contains(province)) {
                    newBlock = Block.RED_CONCRETE;
                }
                shownBlocks.put(province, newBlock);
                PacketUtils.sendGroupedPacket(activePlayers, new BlockChangePacket(province.getPos(), newBlock));
            }
        });
    }

    public void addProvinceDemand(Province province) {
        if (demandedProvinces.contains(province)) return;
        demandedProvinces.add(province);
        updateProvinceBlock(province, Block.RED_CONCRETE);
    }

    public void addPuppetDemand(Country country) {
        if (demandedPuppets.contains(country)) return;
        demandedPuppets.add(country);
        processCountryDemand(country, Block.ORANGE_CONCRETE, true);
    }

    public void addPaymentDemand(Payment payment) {
        demandedPayments.add(payment);
    }

    public void addAnnexationDemand(Country country) {
        if (demandedAnnexation.contains(country)) return;
        demandedAnnexation.add(country);
        processCountryDemand(country, Block.RED_CONCRETE, true);
    }

    public void removeAnnexationDemand(Country country) {
        if (!demandedAnnexation.contains(country)) return;
        demandedAnnexation.remove(country);
        processCountryDemand(country, Block.RED_CONCRETE, false);
    }

    public void removeProvinceDemand(Province province) {
        if (!demandedProvinces.contains(province)) return;
        demandedProvinces.remove(province);
        shownBlocks.remove(province);
        PacketUtils.sendGroupedPacket(activePlayers, new BlockChangePacket(province.getPos(), province.getMaterial().block()));
    }

    public void removePuppetsDemand(Country country) {
        if (!demandedPuppets.contains(country)) return;
        demandedPuppets.remove(country);
        processCountryDemand(country, Block.ORANGE_CONCRETE, false);
    }

    public void addProvinceOffer(Province province) {
        if (offeredProvinces.contains(province)) return;
        offeredProvinces.add(province);
        updateProvinceBlock(province, Block.GREEN_CONCRETE);
    }

    public void addPuppetOffer(Country country) {
        if (offeredPuppets.contains(country)) return;
        offeredPuppets.add(country);
        processCountryDemand(country, Block.LIME_CONCRETE, true);
    }

    public void addPaymentOffer(Payment payment) {
        offeredPayments.add(payment);
    }

    public void addAnnexationOffer(Country country) {
        if (offeredAnnexation.contains(country)) return;
        offeredAnnexation.add(country);
        processCountryDemand(country, Block.GREEN_CONCRETE, true);
    }

    public void removeAnnexationOffer(Country country) {
        if (!offeredAnnexation.contains(country)) return;
        offeredAnnexation.remove(country);
        processCountryDemand(country, Block.GREEN_CONCRETE, false);
    }

    public void removeProvinceOffer(Province province) {
        if (!offeredProvinces.contains(province)) return;
        offeredProvinces.remove(province);
        shownBlocks.remove(province);
        PacketUtils.sendGroupedPacket(activePlayers, new BlockChangePacket(province.getPos(), province.getMaterial().block()));
    }

    public void removePuppetsOffer(Country country) {
        if (!offeredPuppets.contains(country)) return;
        offeredPuppets.remove(country);
        processCountryDemand(country, Block.ORANGE_CONCRETE, false);
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
