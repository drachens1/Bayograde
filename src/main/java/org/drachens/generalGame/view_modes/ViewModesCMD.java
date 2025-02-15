package org.drachens.generalGame.view_modes;

import net.minestom.server.command.builder.Command;

public class ViewModesCMD extends Command {
    public ViewModesCMD() {
        super("view-modes");
        addSubcommand(new AllyViewModeCMD());
        addSubcommand(new WarsViewModeCMD());
    }
}
