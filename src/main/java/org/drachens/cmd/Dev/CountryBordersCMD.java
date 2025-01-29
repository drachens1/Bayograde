package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class CountryBordersCMD extends Command {
    public CountryBordersCMD() {
        super("country-borders");

        var s = ArgumentType.String("country-borders")
                .setSuggestionCallback(((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    Country country = p.getCountry();
                    getSuggestionBasedOnInput(suggestion,country.getBorders());
                }));

        var a = ArgumentType.Word("something")
                        .from("country","borders");

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Instance instance = country.getInstance();
            Country c = ContinentalManagers.world(instance).countryDataManager().getCountryFromName(context.get(s));
            switch (context.get(a)){
                case "country":
                    c.getOccupies().forEach(province -> {
                        instance.setBlock(province.getPos(),Material.DIAMOND_BLOCK.block());
                    });
                    break;
                case "borders":
                    country.getBordersCountry(context.get(s)).forEach(province ->
                            instance.setBlock(province.getPos(), Material.DIAMOND_BLOCK.block()));
                    c.getBordersCountry(country.getName()).forEach(province ->
                            instance.setBlock(province.getPos(), Material.GOLD_BLOCK.block()));
                    break;
            }
        },s,a);
    }
}
