package org.drachens.cmd.Dev.serverManagement;

import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;

import static org.drachens.util.ServerUtil.stopSrv;

public class StopCMD extends Command {
    private final String permission = "stop";
    public StopCMD() {
        super("stop");
        setCondition((sender,s)->sender.hasPermission(permission));
        setDefaultExecutor((sender,context)->{
            if (sender instanceof ConsoleSender){
                stopSrv();
                return;
            }
            if (sender.hasPermission(permission))stopSrv();
        });
    }
}
