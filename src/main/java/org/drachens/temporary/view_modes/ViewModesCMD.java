package org.drachens.temporary.view_modes;

import net.minestom.server.command.builder.Command;

public class ViewModesCMD extends Command {
    public ViewModesCMD() {
        super("view_modes");
        addSubcommand(new AllyViewModeCMD());
        addSubcommand(new WarsViewModeCMD());
    }
}
