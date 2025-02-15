package org.drachens.generalGame.demand;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.ImaginaryWorld;
import org.drachens.dataClasses.Province;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WW2Demands extends Demand {
    private final HashMap<Province, Block> shownBlocks = new HashMap<>();
    private final ImaginaryWorld imaginaryWorld;
    InventoryManager inventoryManager = ContinentalManagers.inventoryManager;
    private List<Country> demandedAnnexation = new ArrayList<>();
    private List<Province> demandedProvinces = new ArrayList<>();
    private List<Country> demandedPuppets = new ArrayList<>();
    private List<Payment> demandedPayments = new ArrayList<>();
    private List<Payment> offeredPayments = new ArrayList<>();
    private List<Country> offeredPuppets = new ArrayList<>();
    private List<Province> offeredProvinces = new ArrayList<>();
    private List<Country> offeredAnnexation = new ArrayList<>();
    private boolean peace;

    public WW2Demands(Country from, Country to) {
        super(from, to);
        imaginaryWorld = new ImaginaryWorld(to.getInstance(), true);
        List<Country> countries = new ArrayList<>(ContinentalManagers.world(to.getInstance()).countryDataManager().getCountries());
        countries.remove(to);
        countries.removeAll(to.getPuppets());
        countries.remove(from);
        countries.removeAll(from.getPuppets());
        countries.forEach(country -> country.getOccupies().forEach(province -> imaginaryWorld.addGhostBlock(province.getPos(), Block.GRAY_CONCRETE)));
        shownBlocks.forEach((province, block) -> imaginaryWorld.addGhostBlock(province.getPos(), block));
        from.getPlayers().forEach(Player::refreshCommands);
    }

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

    @Override
    public Component description() {
        List<Component> comps = new ArrayList<>();
        if (!(offeredPuppets.isEmpty() && offeredPayments.isEmpty() && offeredProvinces.isEmpty() && offeredAnnexation.isEmpty())) {
            comps.add(Component.text()
                    .appendNewline()
                    .append(Component.text("Offered: ", NamedTextColor.GREEN, TextDecoration.BOLD, TextDecoration.UNDERLINED))
                    .build());

            if (!offeredPuppets.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Puppets: "))
                        .build());
                for (Country c : offeredPuppets) {
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(c.getNameComponent())
                            .build());
                }
            }

            if (!offeredPayments.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Payments: "))
                        .build());
                for (Payment p : offeredPayments) {
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(p.getAmount()))
                            .append(p.getCurrencyType().getSymbol())
                            .build());
                }
            }

            if (!offeredProvinces.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Provinces: "))
                        .build());
                for (Province p : offeredProvinces) {
                    Pos pos = p.getPos();
                    String merge = pos.x() + ", " + pos.y();
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(merge))
                            .build());
                }
            }

            if (!offeredAnnexation.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Offered Annexation: "))
                        .build());
                for (Country c : offeredAnnexation) {
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(c.getNameComponent())
                            .build());
                }
            }
        }

        if (!(demandedPuppets.isEmpty() && demandedPayments.isEmpty() && demandedProvinces.isEmpty() && demandedAnnexation.isEmpty())) {
            comps.add(Component.text()
                    .appendNewline()
                    .append(Component.text("Demanded: ", NamedTextColor.RED, TextDecoration.BOLD, TextDecoration.UNDERLINED))
                    .build());

            if (!demandedPuppets.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Demanded Puppets: "))
                        .build());
                for (Country c : demandedPuppets) {
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(c.getNameComponent())
                            .build());
                }
            }

            if (!demandedPayments.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Demanded Payments: "))
                        .build());
                for (Payment p : demandedPayments) {
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(p.getAmount()))
                            .append(p.getCurrencyType().getSymbol())
                            .build());
                }
            }

            if (!demandedProvinces.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Demanded Provinces: "))
                        .build());
                for (Province p : demandedProvinces) {
                    Pos pos = p.getPos();
                    String merge = pos.x() + ", " + pos.y();
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(Component.text(merge))
                            .build());
                }
            }

            if (!demandedAnnexation.isEmpty()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("- Demanded Annexation: "))
                        .build());
                for (Country c : demandedAnnexation) {
                    comps.add(Component.text()
                            .appendNewline()
                            .append(Component.text(" - "))
                            .append(c.getNameComponent())
                            .build());
                }
            }
        }

        if (getFromCountry().isAtWar(getToCountry())) {
            comps.add(Component.text()
                    .appendNewline()
                    .append(Component.text("Peace: ", NamedTextColor.GREEN, TextDecoration.BOLD))
                    .build());
            if (peace) {
                comps.add(Component.text(String.valueOf(true), NamedTextColor.GREEN));
            } else
                comps.add(Component.text(String.valueOf(false), NamedTextColor.RED));
        }

        return Component.text()
                .append(Component.text("_________/", NamedTextColor.BLUE))
                .append(Component.text("Demand", NamedTextColor.RED, TextDecoration.BOLD))
                .append(Component.text("\\_________\n", NamedTextColor.BLUE))
                .append(comps)
                .appendNewline()
                .build();
    }

    @Override
    public void ifAccepted() {
        getFromCountry().removeOutgoingDemand(this);
        Country to = getToCountry();
        Country from = getFromCountry();

        demandedAnnexation.forEach(country -> new ArrayList<>(country.getOccupies()).forEach(province -> province.setOccupier(from)));
        demandedPuppets.forEach(country -> {
            country.setOverlord(from);
            from.addPuppet(country);
        });
        demandedProvinces.forEach(province -> province.setOccupier(from));
        demandedPayments.forEach(payment -> to.minusThenLoan(payment, from));
        offeredAnnexation.forEach(country -> new ArrayList<>(country.getOccupies()).forEach(province -> province.setOccupier(to)));
        offeredPuppets.forEach(country -> {
            country.setOverlord(to);
            to.addPuppet(country);
        });
        offeredProvinces.forEach(province -> province.setOccupier(to));
        offeredPayments.forEach(payment -> from.minusThenLoan(payment, to));
    }

    @Override
    public void ifDenied() {
        getFromCountry().removeOutgoingDemand(this);
    }

    @Override
    protected void onCompleted() {
        imaginaryWorld.getPlayers().forEach(player -> hidePlayer((CPlayer) player));
        getFromCountry().addOutgoingDemand(this);
    }

    @Override
    public void copyButOpposite(Demand d) {
        if (!(d instanceof WW2Demands demand)) return;
        getFromCountry().removeOutgoingDemand(this);
        this.offeredProvinces = demand.getDemandedProvinces();
        this.offeredAnnexation = demand.getDemandedAnnexation();
        this.offeredPayments = demand.getDemandedPayments();
        this.offeredPuppets = demand.getDemandedPuppets();

        this.demandedProvinces = demand.getOfferProvinces();
        this.demandedAnnexation = demand.getOfferedAnnexation();
        this.demandedPayments = demand.getOfferPayments();
        this.demandedPuppets = demand.getOfferPuppets();
    }

    public void showPlayer(CPlayer p) {
        imaginaryWorld.addPlayer(p);
    }

    public void hidePlayer(CPlayer p) {
        inventoryManager.assignInventory(p, ContinentalManagers.world(p.getInstance()).dataStorer().votingOption.getDefaultInventory());
        imaginaryWorld.removePlayer(p);
    }

    private void updateProvinceBlock(Province province, Block block) {
        shownBlocks.put(province, block);
        imaginaryWorld.addGhostBlock(province.getPos(), block);
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
                imaginaryWorld.addGhostBlock(province.getPos(), newBlock);
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
        imaginaryWorld.addGhostBlock(province.getPos(), province.getMaterial().block());
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
        imaginaryWorld.addGhostBlock(province.getPos(), province.getMaterial().block());
    }

    public void removePuppetsOffer(Country country) {
        if (!offeredPuppets.contains(country)) return;
        offeredPuppets.remove(country);
        processCountryDemand(country, Block.ORANGE_CONCRETE, false);
    }

    public void resetDemandedProvinces() {
        demandedProvinces.clear();
    }

    public void resetDemandedPuppets() {
        demandedPuppets.clear();
    }

    public void resetDemandedPayments() {
        demandedPayments.clear();
    }

    public void resetDemandedAnnexation() {
        demandedAnnexation.clear();
    }

    public void resetOfferProvinces() {
        offeredProvinces.clear();
    }

    public void resetOfferPuppets() {
        offeredPuppets.clear();
    }

    public void resetOfferPayments() {
        offeredPayments.clear();
    }

    public void resetOfferAnnexation() {
        offeredAnnexation.clear();
    }

    public void setPeace(boolean p) {
        peace = p;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray demandAnnex = new JsonArray();
        demandedAnnexation.forEach(country -> demandAnnex.add(country.getReference()));
        jsonObject.add("demandAnnexation", demandAnnex);

        JsonArray demandProv = new JsonArray();
        demandedProvinces.forEach(province -> demandProv.add(province.getReference()));
        jsonObject.add("demandProvinces", demandProv);

        JsonArray demandPuppets = new JsonArray();
        demandedPuppets.forEach(country -> demandPuppets.add(country.getReference()));
        jsonObject.add("demandPuppets", demandPuppets);

        JsonArray demandPayments = new JsonArray();
        demandedPayments.forEach(payment -> demandPayments.add(payment.toJson()));
        jsonObject.add("demandPayments", demandPayments);

        JsonArray offerAnnex = new JsonArray();
        offeredAnnexation.forEach(country -> offerAnnex.add(country.getReference()));
        jsonObject.add("offeredAnnexation", offerAnnex);

        JsonArray offerProv = new JsonArray();
        offeredProvinces.forEach(province -> offerProv.add(province.getReference()));
        jsonObject.add("offeredProvinces", offerProv);

        JsonArray offerPuppets = new JsonArray();
        offeredPuppets.forEach(country -> offerPuppets.add(country.getReference()));
        jsonObject.add("offeredPuppets", offerPuppets);

        JsonArray offerPayments = new JsonArray();
        offeredPayments.forEach(payment -> offerPayments.add(payment.toJson()));
        jsonObject.add("offeredPayments", offerPayments);
        return jsonObject;
    }
}
