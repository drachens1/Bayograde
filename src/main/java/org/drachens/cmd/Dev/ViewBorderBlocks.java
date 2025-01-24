package org.drachens.cmd.Dev;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.item.Material;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class ViewBorderBlocks extends Command {
    public ViewBorderBlocks() {
        super("view-border");

        var countries = ArgumentType.String("options")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getBorders());
                });


        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            p.getCountry().getBordersCountry(context.get(countries)).forEach(province -> {
                province.setBlock(Material.DIAMOND_BLOCK);
            });
        }, countries);
    }
}
