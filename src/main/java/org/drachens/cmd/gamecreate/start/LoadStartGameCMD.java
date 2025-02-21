package org.drachens.cmd.gamecreate.start;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class LoadStartGameCMD extends Command {
    public LoadStartGameCMD() {
        super("load");

        ArgumentString name = ArgumentType.String("name");

        addSyntax((sender, context)->{

        });
    }
}
