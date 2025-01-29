package org.drachens.cmd;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.builder.Command;

import java.time.LocalTime;

public class PlaytimeCMD extends Command {
    public PlaytimeCMD() {
        super("playtime");
        setDefaultExecutor((sender, context) -> {
            CPlayer cPlayer = (CPlayer) sender;
            cPlayer.addPlayTime(LocalTime.now());
            cPlayer.sendMessage(cPlayer.getPlayTimeString());
        });
    }
}
