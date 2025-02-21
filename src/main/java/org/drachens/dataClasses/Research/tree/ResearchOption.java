package org.drachens.dataClasses.Research.tree;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Research.ResearchCountry;
import org.drachens.dataClasses.additional.Modifier;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResearchOption {
    private final Component descript;
    private final Component name;
    private final String identifier;
    private final List<String> requiresString;
    private final List<String> orString;
    private final ItemStack item;
    private final Modifier modifier;
    private final Payment cost;

    protected ResearchOption(Create create) {
        identifier = create.identifier;
        requiresString = create.requires;
        item = create.item;
        modifier = create.modifier;
        orString = create.or;
        cost = create.cost;
        name = create.name;
        descript = create.descript;
    }

    public boolean canResearch(ResearchCountry country) {
        return !country.isResearching() && country.hasResearchedAll(this.requiresString) && !country.hasResearchedAny(this.orString) && !country.hasResearched(identifier);
    }

    public Payment getCost() {
        return cost.clone();
    }

    public List<String> getOr() {
        return orString;
    }

    public List<String> getRequires() {
        return requiresString;
    }

    public static class Create {
        private final String identifier;
        private final ItemStack item;
        private final List<String> requires = new ArrayList<>();
        private final List<String> or = new ArrayList<>();
        private final Payment cost;
        private Modifier modifier;
        private Component name;
        private Component descript;

        public Create(String identifier, ItemStack item, float cost) {
            this.identifier = identifier;
            this.item = item;
            this.cost = new Payment(CurrencyEnum.research, cost);
        }

        public Create addRequires(String identifier) {
            requires.add(identifier);
            return this;
        }

        public Create addOr(String identifier) {
            or.add(identifier);
            return this;
        }

        public Create setModifier(Modifier modifier) {
            this.modifier = modifier;
            return this;
        }

        public Create setName(Component component) {
            this.name = component;
            return this;
        }

        public ResearchOption build() {
            List<Component> comps = new ArrayList<>();
            if (!requires.isEmpty()) {
                comps.add(Component.text()
                        .append(Component.text("Requires: "))
                        .appendNewline()
                        .build());
                requires.forEach(require -> comps.add(Component.text()
                        .append(Component.text(" - "))
                        .append(Component.text(require))
                        .appendNewline()
                        .build()));
            }
            if (!or.isEmpty()) {
                comps.add(Component.text()
                        .append(Component.text("Or: "))
                        .appendNewline()
                        .build());
                or.forEach(string -> comps.add(Component.text()
                        .append(Component.text(" - "))
                        .append(Component.text(string))
                        .appendNewline()
                        .build()));
            }
            if (null != this.modifier && !modifier.getBoostHashMap().isEmpty()) {
                comps.add(Component.text()
                        .append(Component.text("Boosts: "))
                        .appendNewline().build());
                modifier.getBoostHashMap().forEach((boostEnum, aFloat) -> {
                    if (0 > aFloat) {
                        comps.add(Component.text()
                                .append(Component.text(aFloat))
                                .append(boostEnum.getNegSymbol())
                                .build());
                    } else {
                        comps.add(Component.text()
                                .append(Component.text(aFloat))
                                .append(boostEnum.getPosSymbol())
                                .build());
                    }
                });
            }
            descript = Component.text()
                    .append(comps)
                    .build();
            name = name.hoverEvent(HoverEvent.showText(descript));
            name = name.clickEvent(ClickEvent.runCommand("/country research start " + identifier));
            return new ResearchOption(this);
        }
    }
}
