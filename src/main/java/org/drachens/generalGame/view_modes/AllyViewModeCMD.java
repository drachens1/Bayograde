package org.drachens.generalGame.view_modes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.ImaginaryWorld;
import org.drachens.player_types.CPlayer;

public class AllyViewModeCMD extends Command {
    public AllyViewModeCMD() {
        super("ally");
        Argument<String> on = ArgumentType.String("")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (notInCountry(sender)) return;
                    suggestion.addEntry(new SuggestionEntry("on"));
                    suggestion.addEntry(new SuggestionEntry("off"));
                });

        setDefaultExecutor((sender, context) -> sender.sendMessage(Component.text("Proper usage /view-modes ally on/off", NamedTextColor.RED)));

        addSyntax((sender, context) -> {
            if (notInCountry(sender)) {
                sender.sendMessage("Join a country first");
                return;
            }
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            ImaginaryWorld world = country.getMilitary().getAllyWorld();
            switch (context.get(on)) {
                case "on":
                    world.addPlayer(p);
                    break;
                case "off":
                    world.removePlayer(p);
                    break;
            }
        }, on);

    }

    private boolean notInCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            return null == country;
        }
        return true;
    }
}
