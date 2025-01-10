package org.drachens.dataClasses.additional.greatDepression;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.additional.ModifierCommand;

public class GreatDepression implements ModifierCommand {
    private final Component notEnoughTime = Component.text("It has not been 70 days since the last decision",NamedTextColor.RED);
    private final Component alreadyTaken = Component.text("You have already completed this decision",NamedTextColor.RED);
    private final Component properUsage = Component.text("Proper usage /country modifiers depression ", NamedTextColor.RED);
    @Override
    public void getSuggestion(CPlayer p, CommandContext context, Suggestion suggestion) {
        Country country = p.getCountry();
        Modifier modifier = country.getModifier("great_depression");
        GreatDepressionEventsRunner eventsRunner = (GreatDepressionEventsRunner) modifier.getEventsRunners().getFirst();

        if (!eventsRunner.isProtectionismComp()) suggestion.addEntry(new SuggestionEntry("protectionism"));
        if (!eventsRunner.isDevalueCurrencyComp()) suggestion.addEntry(new SuggestionEntry("devalue-currency"));
        if (!eventsRunner.isAbandonGoldStandard()) suggestion.addEntry(new SuggestionEntry("abandon-gold-standard"));
        if (!eventsRunner.isRelief()) suggestion.addEntry(new SuggestionEntry("relief"));
        if (!eventsRunner.isRecovery()) suggestion.addEntry(new SuggestionEntry("recovery"));
        if (!eventsRunner.isReform()) suggestion.addEntry(new SuggestionEntry("reform"));
        suggestion.addEntry(new SuggestionEntry("weekly-effects"));
        suggestion.addEntry(new SuggestionEntry("what-it-do"));
    }

    @Override
    public String getString() {
        return "depression";
    }

    @Override
    public void execute(CPlayer p, String input) {
        Country country = p.getCountry();
        Modifier modifier = country.getModifier("great_depression");
        GreatDepressionEventsRunner eventsRunner = (GreatDepressionEventsRunner) modifier.getEventsRunners().getFirst();
        if (eventsRunner.getTimeSinceLast()<70){
            p.sendMessage(notEnoughTime);
            return;
        }
        switch (input){
            case "protectionism":
                if (eventsRunner.isProtectionismComp()){
                    p.sendMessage(alreadyTaken);
                    return;
                }
                eventsRunner.setProtectionismComp(true);
                p.sendMessage(Component.text()
                        .append(Component.text("Added a weekly boost of: "))
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("0.05f",NamedTextColor.GREEN))
                                .append(BoostEnum.production.getPosSymbol())
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("-0.05f",NamedTextColor.RED))
                                .append(BoostEnum.relations.getNegSymbol())
                        .build());
                eventsRunner.addBoost(BoostEnum.production,0.05f);
                eventsRunner.addBoost(BoostEnum.relations,-0.05f);
                eventsRunner.setTimeSinceLast(0);
                break;
            case "devalue-currency":
                if (eventsRunner.isDevalueCurrencyComp()){
                    p.sendMessage(alreadyTaken);
                    return;
                }
                eventsRunner.setDevalueCurrencyComp(true);
                p.sendMessage(Component.text()
                        .append(Component.text("Added a weekly boost of: "))
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.1f",NamedTextColor.GREEN))
                        .append(BoostEnum.production.getPosSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("-0.05f",NamedTextColor.RED))
                        .append(BoostEnum.stabilityBase.getNegSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.1f",NamedTextColor.GREEN))
                        .append(BoostEnum.gunCost.getPosSymbol())
                        .build());
                eventsRunner.addBoost(BoostEnum.production,0.1f);
                eventsRunner.addBoost(BoostEnum.stabilityBase,-0.05f);
                eventsRunner.addBoost(BoostEnum.gunCost,0.1f);
                eventsRunner.setTimeSinceLast(0);
                break;
            case "abandon-gold-standard":
                if (eventsRunner.isAbandonGoldStandard()){
                    p.sendMessage(alreadyTaken);
                    return;
                }
                eventsRunner.setAbandonGoldStandard(true);
                p.sendMessage(Component.text()
                        .append(Component.text("Added a weekly boost of: "))
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("0.05f",NamedTextColor.GREEN))
                                .append(BoostEnum.production.getPosSymbol())
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("-0.2f",NamedTextColor.RED))
                                .append(BoostEnum.relations.getNegSymbol())
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("0.05f",NamedTextColor.RED))
                                .append(BoostEnum.stabilityBase.getPosSymbol())
                        .build());
                eventsRunner.addBoost(BoostEnum.production,0.05f);
                eventsRunner.addBoost(BoostEnum.relations,-0.2f);
                modifier.addBoost(BoostEnum.stabilityBase,0.05f);
                eventsRunner.setTimeSinceLast(0);
                break;
            case "relief":
                if (eventsRunner.isRelief()){
                    p.sendMessage(alreadyTaken);
                    return;
                }
                eventsRunner.setRelief(true);
                p.sendMessage(Component.text()
                        .append(Component.text("Added a weekly boost of: "))
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("0.05f",NamedTextColor.GREEN))
                                .append(BoostEnum.production.getPosSymbol())
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("-0.05f",NamedTextColor.RED))
                                .append(BoostEnum.relations.getNegSymbol())
                        .build());
                eventsRunner.addBoost(BoostEnum.production,0.05f);
                eventsRunner.addBoost(BoostEnum.recruitablePop,0.05f);
                eventsRunner.addBoost(BoostEnum.stabilityBase,0.2f);
                eventsRunner.setTimeSinceLast(0);
                break;
            case "recovery":
                if (eventsRunner.isRecovery()){
                    p.sendMessage(alreadyTaken);
                    return;
                }
                eventsRunner.setRecovery(true);
                p.sendMessage(Component.text()
                        .append(Component.text("Added a weekly boost of: "))
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("0.1f",NamedTextColor.GREEN))
                                .append(BoostEnum.production.getPosSymbol())
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("0.1f",NamedTextColor.GREEN))
                                .append(BoostEnum.stabilityBase.getPosSymbol())
                        .build());
                eventsRunner.addBoost(BoostEnum.production,0.1f);
                eventsRunner.addBoost(BoostEnum.stabilityBase,0.1f);
                eventsRunner.setTimeSinceLast(0);
                break;
            case "reform":
                if (eventsRunner.isReform()){
                    p.sendMessage(alreadyTaken);
                    return;
                }
                eventsRunner.setReform(true);
                p.sendMessage(Component.text()
                        .append(Component.text("Added a weekly boost of: "))
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("0.1f",NamedTextColor.GREEN))
                                .append(BoostEnum.production.getPosSymbol())
                                .appendNewline()
                                .append(Component.text(" - "))
                                .append(Component.text("-0.05f",NamedTextColor.RED))
                                .append(BoostEnum.stabilityBase.getNegSymbol())
                        .build());
                eventsRunner.addBoost(BoostEnum.production,0.1f);
                eventsRunner.addBoost(BoostEnum.stabilityBase,-0.05f);
                eventsRunner.setTimeSinceLast(0);
                break;
            case "weekly-effects":
                p.sendMessage(eventsRunner.getDescription());
                break;
            case "what-it-do":
                p.sendMessage(Component.text()
                        .append(Component.text("_______/",NamedTextColor.BLUE))
                        .append(Component.text("What it do"))
                        .append(Component.text("\\_______",NamedTextColor.BLUE))
                        .appendNewline()
                        .append(Component.text("Protectionism: "))
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.05f",NamedTextColor.GREEN))
                        .append(BoostEnum.production.getPosSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("-0.05f",NamedTextColor.RED))
                        .append(BoostEnum.relations.getNegSymbol())
                        .append(Component.text("Devalue currency: "))
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.1f",NamedTextColor.GREEN))
                        .append(BoostEnum.production.getPosSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("-0.05f",NamedTextColor.RED))
                        .append(BoostEnum.stabilityBase.getNegSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.1f",NamedTextColor.GREEN))
                        .append(BoostEnum.gunCost.getPosSymbol())
                        .appendNewline()
                        .append(Component.text("Abandon gold standard: "))
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.05f",NamedTextColor.GREEN))
                        .append(BoostEnum.production.getPosSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("-0.2f",NamedTextColor.RED))
                        .append(BoostEnum.relations.getNegSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.05f",NamedTextColor.RED))
                        .append(BoostEnum.stabilityBase.getPosSymbol())
                        .appendNewline()
                        .append(Component.text("Relief: "))
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.05f",NamedTextColor.GREEN))
                        .append(BoostEnum.production.getPosSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("-0.05f",NamedTextColor.RED))
                        .append(BoostEnum.relations.getNegSymbol())
                        .appendNewline()
                        .append(Component.text("Recovery: "))
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.1f",NamedTextColor.GREEN))
                        .append(BoostEnum.production.getPosSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.1f",NamedTextColor.GREEN))
                        .append(BoostEnum.stabilityBase.getPosSymbol())
                        .appendNewline()
                        .append(Component.text("Reform: "))
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("0.1f",NamedTextColor.GREEN))
                        .append(BoostEnum.production.getPosSymbol())
                        .appendNewline()
                        .append(Component.text(" - "))
                        .append(Component.text("-0.05f",NamedTextColor.RED))
                        .append(BoostEnum.stabilityBase.getNegSymbol())
                        .appendNewline()
                        .append(Component.text("Can only do them once",NamedTextColor.GRAY,TextDecoration.ITALIC))
                        .build());
                break;//So it just funds effects every weak to recover the economy
        }
    }

    @Override
    public void properUsage(CPlayer p, CommandContext context) {
        p.sendMessage(properUsage);
    }


}
