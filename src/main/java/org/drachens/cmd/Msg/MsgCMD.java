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

public class MsgCMD extends Command {
    public MsgCMD() {
        super("msg");
        var player = ArgumentType.Entity("player");
        var msg = ArgumentType.StringArray("msg");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /msg <player> <msg>"));
        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Player ps = context.get(player).findFirstPlayer(sender);
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
        p.sendMessage(Component.text()
                .append(Component.text("To ", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(to.getUsername(), NamedTextColor.GOLD))
                .append(Component.text(" : ", NamedTextColor.GOLD))
                .append(Component.text(msg, NamedTextColor.GOLD))
                .build());
        to.sendMessage(Component.text()
                .append(Component.text("From ", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(p.getUsername(), NamedTextColor.GOLD))
                .append(Component.text(" : ", NamedTextColor.GOLD))
                .append(Component.text(msg, NamedTextColor.GOLD))
                .build());
        to.playSound(Sound.sound(SoundEvent.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 1f, 1f));
    }
}