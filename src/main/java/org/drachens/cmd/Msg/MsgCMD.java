package org.drachens.cmd.Msg;

import dev.ng5m.CPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.sound.SoundEvent;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;
import static org.drachens.util.KyoriUtil.mergeComp;

public class MsgCMD extends Command {
    public MsgCMD() {
        super("msg");
        var player = ArgumentType.Entity("player");
        var msg = ArgumentType.StringArray("msg");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /msg <player> <msg>"));
        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Player ps = context.get(player).findFirstPlayer(sender);
            if (p == ps) {
                p.sendMessage("You cannot msg yourself silly");
                return;
            }
            if (ps == null) {
                p.sendMessage("Player not found");
                return;
            }
            msgBuild(p, ps, context.get(msg));
            p.setLastMessenger(ps);
        }, player, msg);
    }

    private String buildString(String[] msg) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : msg) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString();
    }

    private void msgBuild(Player p, Player to, String[] msgs) {
        String msg = buildString(msgs);
        List<Component> toComp = new ArrayList<>();
        toComp.add(compBuild("To ", NamedTextColor.GOLD, TextDecoration.BOLD));
        toComp.add(compBuild(to.getUsername(), NamedTextColor.GOLD));
        toComp.add(compBuild(" : ", NamedTextColor.GOLD));
        toComp.add(compBuild(msg, NamedTextColor.GOLD));
        p.sendMessage(mergeComp(toComp));
        toComp.clear();
        toComp.add(compBuild("From ", NamedTextColor.GOLD, TextDecoration.BOLD));
        toComp.add(compBuild(p.getUsername(), NamedTextColor.GOLD));
        toComp.add(compBuild(" : ", NamedTextColor.GOLD));
        toComp.add(compBuild(msg, NamedTextColor.GOLD));
        to.sendMessage(mergeComp(toComp));
        to.playSound(Sound.sound(SoundEvent.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 1f, 1f));
    }
}