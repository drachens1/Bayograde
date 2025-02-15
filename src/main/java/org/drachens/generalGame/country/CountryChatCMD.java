package org.drachens.generalGame.country;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.CountryChat;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.HashMap;

public class CountryChatCMD extends Command {
    private final HashMap<CPlayer, Boolean> active = new HashMap<>();

    public CountryChatCMD() {
        super("chat");

        setCondition((sender, s) -> isInCountry(sender));

        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            CountryChat countryChat = p.getCountry().getCountryChat();
            boolean current = active.getOrDefault(p, false);
            current = !current;
            active.put(p, current);
            if (current) {
                countryChat.addPlayer(p);
                p.sendMessage(Component.text()
                        .append(MessageEnum.faction.getComponent())
                        .append(Component.text("Country chat is now active")));
            } else {
                countryChat.removePlayer(p);
                p.sendMessage(Component.text()
                        .append(MessageEnum.faction.getComponent())
                        .append(Component.text("Country chat is now inactive")));
            }
        });
    }

    private boolean isInCountry(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null;
        }
        return false;
    }
}
