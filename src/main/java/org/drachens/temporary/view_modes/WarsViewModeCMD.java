package org.drachens.temporary.view_modes;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.ImaginaryWorld;

public class WarsViewModeCMD extends Command {
    public WarsViewModeCMD(){
        super("wars");
        var on = ArgumentType.String("")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!inCountry(sender)) return;
                    suggestion.addEntry(new SuggestionEntry("on"));
                    suggestion.addEntry(new SuggestionEntry("off"));
                });

        addSyntax((sender, context) -> {
            if (!inCountry(sender)){
                sender.sendMessage("Join a country first");
                return;
            }
            CPlayer p = (CPlayer)sender;
            Country country = p.getCountry();
            ImaginaryWorld world = country.getWarsWorld();
            switch (context.get(on)){
                case "on":
                    world.addPlayer(p);
                    break;
                case "off":
                    world.removePlayer(p);
                    break;
            }
        },on);

    }

    private boolean inCountry(CommandSender sender){
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            return country != null;
        }
        return false;
    }

}
