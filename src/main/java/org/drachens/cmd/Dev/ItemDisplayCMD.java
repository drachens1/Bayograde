package org.drachens.cmd.Dev;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.other.TextDisplay;

import static org.drachens.util.KyoriUtil.compBuild;

public class ItemDisplayCMD extends Command {
    public ItemDisplayCMD() {
        super("itemDisplay");
        var number = ArgumentType.Integer("Opacity");
        addSyntax((sender,context)->{
            Player p = (Player) sender;
            int num = context.get(number);
            TextDisplay textDisplay = new TextDisplay.create(p.getPosition().add(0,2,0),p.getInstance(),compBuild("apple", NamedTextColor.RED))
                    .setOpacity((byte) num)
                    .setFollowPlayer(true)
                    .build();
            textDisplay.addViewer(p);
        },number);
    }
}
