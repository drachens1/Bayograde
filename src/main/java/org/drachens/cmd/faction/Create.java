package org.drachens.cmd.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.events.Factions.FactionCreateEvent;
import org.drachens.interfaces.BetterCommand.IndividualCMD;

import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class Create extends IndividualCMD {
    public Create() {
        super("create");

        var nameArg = ArgumentType.String("Faction name");
        addSyntax((sender,context)->{
            if (!requirements(sender))
                return;

            Player p = (Player) sender;
            Country country = getCountryFromPlayer(p);
            String name = context.get(nameArg);
            CountryDataManager countryDataManager = ContinentalManagers.world(p.getInstance()).countryDataManager();
            if (countryDataManager.getFactionNames().contains(name)){
                p.sendMessage("You cannot name it the same as another faction");
                return;
            }
            Factions factions = new Factions(country,name, new EconomyFactionType());
            country.setFaction(factions);
            countryDataManager.addFaction(factions);
            EventDispatcher.call(new FactionCreateEvent(country,factions));
        },nameArg);

        setDefaultExecutor((sender,context)->sender.sendMessage("Proper usage /faction create <faction name> "));
    }

    @Override
    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof Player p))return false;
        Country country = getCountryFromPlayer(p);
        return country != null;
    }
}
