package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class PlayersCMD extends Command {
    public PlayersCMD() {
        super("players");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            Country country = p.getCountry();
            if (null == country) {
                p.sendMessage(Component.text("Join a country in order to execute this command", NamedTextColor.RED));
                return;
            }
            p.sendMessage(buildComp(country));
        });

        Argument<String> countries = getCountriesArg();

        addSyntax((sender,context)->{
            if (!(sender instanceof CPlayer p)) return;
            Country target = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == target) {
                p.sendMessage("That is not a valid country");
                return;
            }
            p.sendMessage(buildComp(target));
        },countries);
    }

    public Component buildComp(Country country){
        List<Component> comps = new ArrayList<>();
        country.getInfo().getPlayers().forEach(player -> {
            if (country.isPlayerLeader(player)){
                comps.add(Component.text().appendNewline().append(Component.text(" - ")).append(player.getCPlayerName())
                        .append(Component.text(" (leader)",NamedTextColor.GOLD)).build());
            }else {
                comps.add(Component.text().appendNewline().append(Component.text(" -")).append(player.getCPlayerName()).build());

            }
        });
        return Component.text()
                .append(Component.text("___________/", NamedTextColor.BLUE))
                .append(Component.text("Players"))
                .append(Component.text("\\___________", NamedTextColor.BLUE))
                .append(comps)
                .appendNewline().build();
    }
}
