package org.drachens.cmd.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.events.Factions.FactionCreateEvent;

import java.util.List;

import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class Military {
    public Military(String cmd) {
    }

    public void causes(CommandSender sender, String input) {
        Player p = (Player) sender;
        Country country = getCountryFromPlayer(p);
        String name = input;
        CountryDataManager countryDataManager = ContinentalManagers.world(p.getInstance()).countryDataManager();
        MilitaryFactionType factions = new MilitaryFactionType(country,name);
        country.setMilitaryFactionType(factions);
        countryDataManager.addFaction(factions);
        EventDispatcher.call(new FactionCreateEvent(country,factions));
    }

    public boolean requirements(CommandSender sender) {
        return true;
    }

    public List<String> generateAutoComp(CommandSender sender) {
        return null;
    }
}
